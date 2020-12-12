package com.smartcoding.job.core.executor;

import com.smartcoding.job.core.biz.AdminBiz;
import com.smartcoding.job.core.biz.ExecutorBiz;
import com.smartcoding.job.core.biz.JobBiz;
import com.smartcoding.job.core.biz.impl.ExecutorBizImpl;
import com.smartcoding.job.core.biz.model.JobInfoParam;
import com.smartcoding.job.core.handler.IJobHandler;
import com.smartcoding.job.core.log.XxlJobFileAppender;
import com.smartcoding.job.core.thread.*;
import com.xxl.rpc.registry.ServiceRegistry;
import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
import com.xxl.rpc.serialize.Serializer;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 2016/3/2 21:14.
 */
public class XxlJobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobExecutor.class);

    // ---------------------- param ----------------------
    /**
     * 服务端的地址
     */
    private String adminAddresses;
    /**
     * appName
     */
    private String appName;
    /**
     * 应用名称
     */
    private String title;
    /**
     * 客户端的ip
     */
    private String ip;
    /**
     * 客户端的端口
     */
    private int port;
    /**
     * 接口调用的accessToken
     */
    private String accessToken;
    /**
     * 自动创建任务
     */
    private boolean autoCreateJob;
    /**
     * 创建任务后自动开启
     */
    private boolean autoStartJob;
    /**
     * 客户端的日志路径
     */
    private String logPath;
    /**
     * 客户端的日志保存的天数
     */
    private int logRetentionDays;

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAutoStartJob(boolean autoStartJob) {
        this.autoStartJob = autoStartJob;
    }

    public void setAutoCreateJob(boolean autoCreateJob) {
        this.autoCreateJob = autoCreateJob;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public String getAppName() {
        return appName;
    }

    public boolean isAutoStartJob() {
        return autoStartJob;
    }

    public boolean isAutoCreateJob() {
        return autoCreateJob;
    }

    // ---------------------- start + stop ----------------------
    public void start() throws Exception {

        // init logPath
        XxlJobFileAppender.initLogPath(logPath);

        // init invoker, admin-client
        initAdminBizList(adminAddresses, accessToken);


        // init JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().start(logRetentionDays);

        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init executor-server
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();
        initRpcProvider(ip, port, appName, accessToken, title);
    }

    public void destroy() {
        // destroy executor-server
        stopRpcProvider();
        // destroy jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Long, JobThread> item : jobThreadRepository.entrySet()) {
                removeJobThread(item.getKey(), "web container destroy and kill the job.");
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();


        // destroy JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().toStop();

        // destroy TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();


        // destroy invoker
        stopInvokerFactory();
    }


    // ---------------------- admin-client (rpc invoker) ----------------------
    private static List<AdminBiz> adminBizList;
    private static List<JobBiz> jobBizList;
    private static Serializer serializer;

    private void initAdminBizList(String adminAddresses, String accessToken) throws Exception {
        serializer = Serializer.SerializeEnum.HESSIAN.getSerializer();
        if (adminAddresses != null && adminAddresses.trim().length() > 0) {
            for (String address : adminAddresses.trim().split(",")) {
                if (address != null && address.trim().length() > 0) {

                    String addressUrl = address.concat(AdminBiz.MAPPING);

                    AdminBiz adminBiz = (AdminBiz) new XxlRpcReferenceBean(
                            NetEnum.NETTY_HTTP,
                            serializer,
                            CallType.SYNC,
                            LoadBalance.ROUND,
                            AdminBiz.class,
                            null,
                            3000,
                            addressUrl,
                            accessToken,
                            null,
                            null
                    ).getObject();

                    if (adminBizList == null) {
                        adminBizList = new ArrayList<AdminBiz>();
                    }
                    adminBizList.add(adminBiz);

                    JobBiz jobBiz = (JobBiz) new XxlRpcReferenceBean(
                            NetEnum.NETTY_HTTP,
                            serializer,
                            CallType.SYNC,
                            LoadBalance.ROUND,
                            JobBiz.class,
                            null,
                            6000,
                            addressUrl,
                            accessToken,
                            null,
                            null
                    ).getObject();

                    if (jobBizList == null) {
                        jobBizList = new ArrayList<JobBiz>();
                    }
                    jobBizList.add(jobBiz);
                }
            }
        }
    }

    private void stopInvokerFactory() {
        // stop invoker factory
        try {
            XxlRpcInvokerFactory.getInstance().stop();
        } catch (Exception e) {
            logger.error("stopInvokerFactory error", e);
        }
    }

    public static List<AdminBiz> getAdminBizList() {
        return adminBizList;
    }

    public static List<JobBiz> getJobBizList() {
        return jobBizList;
    }

    public static Serializer getSerializer() {
        return serializer;
    }


    // ---------------------- executor-server (rpc provider) ----------------------
    private XxlRpcProviderFactory xxlRpcProviderFactory = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken, String title) throws Exception {

        // init, provider factory
        String address = IpUtil.getIpPort(ip, port);
        Map<String, String> serviceRegistryParam = new HashMap<String, String>();
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("title", title);
        serviceRegistryParam.put("address", address);

        xxlRpcProviderFactory = new XxlRpcProviderFactory();
        xxlRpcProviderFactory.initConfig(NetEnum.NETTY_HTTP, Serializer.SerializeEnum.HESSIAN.getSerializer(), ip, port, accessToken, ExecutorServiceRegistry.class, serviceRegistryParam);

        // add services
        xxlRpcProviderFactory.addService(ExecutorBiz.class.getName(), null, new ExecutorBizImpl());

        // start
        xxlRpcProviderFactory.start();

    }

    public static class ExecutorServiceRegistry extends ServiceRegistry {

        @Override
        public void start(Map<String, String> param) {
            // start registry
            ExecutorRegistryThread.getInstance().start(param.get("appName"), param.get("address"));

            JobRegistryThread.getInstance().start(param.get("appName"), param.get("title"));
        }

        @Override
        public void stop() {
            // stop registry
            ExecutorRegistryThread.getInstance().toStop();
            JobRegistryThread.getInstance().toStop();
        }

        @Override
        public boolean registry(Set<String> keys, String value) {
            return false;
        }

        @Override
        public boolean remove(Set<String> keys, String value) {
            return false;
        }

        @Override
        public Map<String, TreeSet<String>> discovery(Set<String> keys) {
            return null;
        }

        @Override
        public TreeSet<String> discovery(String key) {
            return null;
        }

    }

    private void stopRpcProvider() {
        // stop provider factory
        try {
            xxlRpcProviderFactory.stop();
        } catch (Exception e) {
            logger.error("stopRpcProvider error", e);
        }
    }


    // ---------------------- job handler repository ----------------------
    private static Map<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();
    private static Map<String, JobInfoParam> jobInfoRepository = new ConcurrentHashMap<String, JobInfoParam>();

    public static IJobHandler registJobHandler(String name, IJobHandler jobHandler) {
        logger.info(">>>>>>>>>>> xxl-job register jobHandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static IJobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }


    // ---------------------- job thread repository ----------------------
    private static Map<Long, JobThread> jobThreadRepository = new ConcurrentHashMap<Long, JobThread>();

    public static JobThread registJobThread(Long jobId, String jobRequestId, IJobHandler handler, String removeOldReason) {
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        logger.info(">>>>>>>>>>> xxl-job regist JobThread success,jobRequestId:{},jobId:{}, handler:{}", jobRequestId, jobId, handler);
        // putIfAbsent | oh my god, map's put method return the old value!!!
        JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }

    public static void removeJobThread(Long jobId, String removeOldReason) {
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
    }

    public static JobThread loadJobThread(Long jobId) {
        JobThread jobThread = jobThreadRepository.get(jobId);
        return jobThread;
    }

    public static JobInfoParam registJobInfoParam(String name, JobInfoParam jobInfoParam) {
        logger.info(">>>>>>>>>>> xxl-job local cache save JobInfoParam success, name:{}, JobInfoParam:{}", name, jobInfoParam);
        return jobInfoRepository.put(name, jobInfoParam);
    }

    public static Map<String, JobInfoParam> loadJobInfoParam() {
        return jobInfoRepository;
    }

    public static void removeJobInfoParam(String name) {
        jobInfoRepository.remove(name);

    }
}
