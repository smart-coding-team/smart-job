package cn.smartcoding.job.core.executor.impl;

import cn.smartcoding.job.core.handler.IJobHandler;
import cn.smartcoding.job.core.handler.annotation.Job;
import cn.smartcoding.job.core.handler.annotation.JobHandler;
import cn.smartcoding.job.core.biz.model.JobInfoParam;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.cron.CronExpression;
import cn.smartcoding.job.core.executor.XxlJobExecutor;
import cn.smartcoding.job.core.glue.GlueFactory;
import cn.smartcoding.job.core.handler.annotation.JobMethodHandler;
import cn.smartcoding.job.core.handler.impl.MethodJobHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * xxl-job executor (for spring)
 *
 * @author xuxueli 2018-11-01 09:24:52
 */
public class XxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware {


    @Override
    public void start() throws Exception {

        Map<String, Object> serviceBeanMap = getServiceBeanMap(applicationContext);
        // init JobHandler Repository
        initJobHandlerRepository(serviceBeanMap);
        //init JobMethodHandler Repository
        initJobHandlerMethodRepository(serviceBeanMap);
        //init JobMethodHandler Repository

        initJobMethodRepository(serviceBeanMap, super.isAutoStartJob(), super.isAutoCreateJob());
        // refresh GlueFactory
        GlueFactory.refreshInstance(1);

        // super start
        super.start();
    }

    private void initJobMethodRepository(Map<String, Object> serviceBeanMap, boolean autoStartJob, boolean autoCreateJob) {
        if (serviceBeanMap == null || serviceBeanMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = serviceBeanMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Job xxlJob = AnnotationUtils.findAnnotation(method, Job.class);
                if (xxlJob == null) {
                    continue;
                }
                //是否开启创建任务
                boolean autoCreatOrUpdate = autoCreateJob && xxlJob.createJob();
                // executorHandler
                String executorHandler = xxlJob.executorHandler();
                if (executorHandler.trim().length() == 0) {
                    throw new RuntimeException("xxl-job method-jobhandler executorHandler invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                }
                if (loadJobHandler(executorHandler) != null) {
                    throw new RuntimeException("xxl-job jobhandler[" + executorHandler + "] naming conflicts.");
                }
                String jobCron = xxlJob.jobCron();
                if (autoCreatOrUpdate && !CronExpression.isValidExpression(jobCron)) {
                    throw new RuntimeException("xxl-job method-job cron invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                }
                String jobName = xxlJob.jobName();
                if (autoCreatOrUpdate && StringUtils.isEmpty(jobName)) {
                    throw new RuntimeException("xxl-job jobName[" + jobName + "] name is not empty.");
                }
                // execute method
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (autoCreatOrUpdate && !(parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(String.class))) {
                    throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                if (autoCreatOrUpdate && !method.getReturnType().isAssignableFrom(ReturnT.class)) {
                    throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                method.setAccessible(true);

                // init and destory
                Method initMethod = null;
                Method destroyMethod = null;

                if (xxlJob.init().trim().length() > 0) {
                    try {
                        initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
                        initMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                if (xxlJob.destroy().trim().length() > 0) {
                    try {
                        destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
                        destroyMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                //registry jobhandler
                registJobHandler(executorHandler, new MethodJobHandler(bean, method, initMethod, destroyMethod));
                if (autoCreatOrUpdate) {
                    JobInfoParam jobInfoParam = new JobInfoParam();
                    jobInfoParam.setAppName(getAppName());
                    jobInfoParam.setJobName(jobName);
                    jobInfoParam.setJobCron(jobCron);
                    jobInfoParam.setExecutorHandler(executorHandler);
                    jobInfoParam.setExecutorParam(xxlJob.executorParam());
                    jobInfoParam.setExecutorRouteStrategy(xxlJob.executorRouteStrategy().name());
                    jobInfoParam.setExecutorBlockStrategy(xxlJob.executorBlockStrategy().name());
                    jobInfoParam.setExecutorTimeout(xxlJob.executorTimeout());
                    jobInfoParam.setExecutorFailRetryCount(xxlJob.executorFailRetryCount());
                    jobInfoParam.setChildJobId(xxlJob.childJobId());
                    jobInfoParam.setAutoStartJob(autoStartJob);
                    //registry jobInfo
                    registJobInfoParam(executorHandler, jobInfoParam);
                }

            }
        }

    }


    private void initJobHandlerRepository(Map<String, Object> serviceBeanMap) {
        // init job handler action
        if (serviceBeanMap == null || serviceBeanMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = serviceBeanMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object serviceBean = next.getValue();
            if (serviceBean instanceof IJobHandler) {
                String name = serviceBean.getClass().getAnnotation(JobHandler.class).value();
                if (name.isEmpty()) {
                    return;
                }
                IJobHandler handler = (IJobHandler) serviceBean;
                if (loadJobHandler(name) != null) {
                    throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
                }
                XxlJobExecutor.registJobHandler(name, handler);
            }
        }
    }

    public Map<String, Object> getServiceBeanMap(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return new HashMap<>();
        }
        return applicationContext.getBeansWithAnnotation(JobHandler.class);
    }

    private void initJobHandlerMethodRepository(Map<String, Object> serviceBeanMap) {
        if (serviceBeanMap == null || serviceBeanMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = serviceBeanMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                JobMethodHandler xxlJob = AnnotationUtils.findAnnotation(method, JobMethodHandler.class);
                if (xxlJob != null) {

                    // name
                    String name = xxlJob.value();
                    if (name.trim().length() == 0) {
                        throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
                    }

                    // execute method
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (!(parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(String.class))) {
                        throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                                "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                    }
                    if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
                        throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                                "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                    }
                    method.setAccessible(true);

                    // init and destory
                    Method initMethod = null;
                    Method destroyMethod = null;

                    if (xxlJob.init().trim().length() > 0) {
                        try {
                            initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
                            initMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                        }
                    }
                    if (xxlJob.destroy().trim().length() > 0) {
                        try {
                            destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
                            destroyMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                        }
                    }

                    // registry jobhandler
                    registJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
                }
            }
        }
    }

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XxlJobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
