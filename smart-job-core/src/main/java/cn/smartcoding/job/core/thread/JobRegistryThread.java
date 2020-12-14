package cn.smartcoding.job.core.thread;

import cn.smartcoding.job.core.biz.JobBiz;
import cn.smartcoding.job.core.biz.JobConst;
import cn.smartcoding.job.core.biz.model.JobGroupParam;
import cn.smartcoding.job.core.biz.model.JobInfoParam;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.enums.RegistryConfig;
import cn.smartcoding.job.core.executor.XxlJobExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xuxueli on 17/3/2.
 */
public class JobRegistryThread {
    private static Logger logger = LoggerFactory.getLogger(JobRegistryThread.class);

    private static JobRegistryThread instance = new JobRegistryThread();

    public static JobRegistryThread getInstance() {
        return instance;
    }

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("JobRegistryThread").daemon(true).build());
    private ScheduledFuture<?> registryScheduledFuture;
    private AtomicBoolean registryToStop = new AtomicBoolean(false);

    private AtomicBoolean jobGroupRegistry = new AtomicBoolean(false);
    private AtomicBoolean jobInfoRegistry = new AtomicBoolean(false);

    public void start(final String appName, final String title) {
        if (appName == null || appName.trim().length() == 0) {
            logger.warn(">>>>>>>>>>>  xxl-job-client, app registry config fail, appName is null.");
            return;
        }
        if (XxlJobExecutor.getJobBizList() == null || XxlJobExecutor.getJobBizList().isEmpty()) {
            logger.warn(">>>>>>>>>>>  xxl-job-client, app registry config fail, jobBiz List is null.");
            return;
        }
        Runnable registryThread = () -> {
            if (jobGroupRegistry.get() && jobInfoRegistry.get()) {
                toStop();
                logger.info(">>>>>>>>>>>  xxl-job-client,app registry and JobInfoParam registry success!");
                return;
            }
            JobBiz jobBiz = XxlJobExecutor.getJobBizList().get(0);
            // valid
            try {
                if (jobGroupRegistry.get()) {
                    return;
                }
                JobGroupParam registryParam = new JobGroupParam(appName, title, JobConst.CLIENT_VERSION);
                try {
                    ReturnT<String> registryResult = jobBiz.registryJobGroup(registryParam);
                    if (registryResult != null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                        jobGroupRegistry.set(true);
                        logger.info(">>>>>>>>>>>  xxl-job-client app registry success, registryParam:{}, registryResult:{}", registryParam, registryResult);
                    } else {
                        logger.info(">>>>>>>>>>>  xxl-job-client app registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                    }
                } catch (Exception e) {
                    logger.error(">>>>>>>>>>>  xxl-job-client app registry error, registryParam:{}", registryParam, e);
                }
            } catch (Exception e) {
                logger.error(">>>>>>>>>>>  xxl-job-client app registry error,", e);
            }
            // valid
            try {
                if (jobInfoRegistry.get()) {
                    return;
                }
                Map<String, JobInfoParam> jobInfoParamMap = XxlJobExecutor.loadJobInfoParam();
                if (jobInfoParamMap == null || jobInfoParamMap.size() == 0) {
                    jobInfoRegistry.set(true);
                    return;
                }
                Iterator<Map.Entry<String, JobInfoParam>> iterator = jobInfoParamMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, JobInfoParam> entry = iterator.next();
                    JobInfoParam jobInfoParam = entry.getValue();
                    String key = entry.getKey();
                    try {
                        ReturnT<String> stringReturnT = jobBiz.registryJobInfo(jobInfoParam);
                        if (stringReturnT != null && ReturnT.SUCCESS_CODE == stringReturnT.getCode()) {
                            XxlJobExecutor.removeJobInfoParam(key);
                            logger.info(">>>>>>>>>>>  xxl-job-client JobInfoParam registry success, jobName:{}, jobHandle:{},registryResult:{}", jobInfoParam.getJobName(), jobInfoParam.getExecutorHandler(), stringReturnT);
                        } else {
                            logger.info(">>>>>>>>>>>  xxl-job-client JobInfoParam registry fail, jobName:{}, jobHandle:{},registryResult:{}", jobInfoParam.getJobName(), jobInfoParam.getExecutorHandler(), stringReturnT);
                        }
                    } catch (Exception e) {
                        logger.error(">>>>>>>>>>>  xxl-job-client JobInfoParam registry error, jobHandle:{}", key, e);
                    }
                }

            } catch (Exception e) {
                logger.error(">>>>>>>>>>>  xxl-job-client JobInfoParam registry error,", e);
            }

        };
        try {
            if (registryToStop.compareAndSet(false, true) && registryScheduledFuture == null) {
                registryScheduledFuture = executorService.scheduleAtFixedRate(registryThread, 10, RegistryConfig.BEAT_TIMEOUT, TimeUnit.SECONDS);
                logger.info(">>>>>>>>>>>  xxl-job-client,Start JobRegistryThread success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>>  xxl-job-client,Start JobRegistryThread failed!", t);
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>>  xxl-job-client, JobRegistryThread  stopping");
            if (registryToStop.compareAndSet(true, false) && registryScheduledFuture != null) {
                registryScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>>  xxl-job-client,Stop JobRegistryThread  success!");
            }
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>>  xxl-job-client,stop  JobRegistryThread  failed!", t);
        }
    }

}
