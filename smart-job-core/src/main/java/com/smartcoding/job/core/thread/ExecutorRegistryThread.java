package com.smartcoding.job.core.thread;

import com.smartcoding.job.core.biz.AdminBiz;
import com.smartcoding.job.core.biz.model.RegistryParam;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.enums.RegistryConfig;
import com.smartcoding.job.core.executor.XxlJobExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xuxueli on 17/3/2.
 */
public class ExecutorRegistryThread {
    private static Logger logger = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    private static ExecutorRegistryThread instance = new ExecutorRegistryThread();

    public static ExecutorRegistryThread getInstance() {
        return instance;
    }

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("ExecutorRegistryThread").daemon(true).build());
    private ScheduledFuture<?> registryScheduledFuture;
    private AtomicBoolean registryToStop = new AtomicBoolean(false);
    private String appName;
    private String address;

    public void start(final String appName, final String address) {
        this.appName = appName;
        this.address = address;
        if (appName == null || appName.trim().length() == 0) {
            logger.warn(">>>>>>>>>>> xxl-job, executor registry config fail, appName is null.");
            return;
        }
        if (XxlJobExecutor.getAdminBizList() == null) {
            logger.warn(">>>>>>>>>>> xxl-job, executor registry config fail, adminAddresses is null.");
            return;
        }
        Runnable registryThread = () -> {
            // valid

            try {
                RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                for (AdminBiz adminBiz : XxlJobExecutor.getAdminBizList()) {
                    try {
                        ReturnT<String> registryResult = adminBiz.registry(registryParam);
                        if (registryResult != null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                            registryResult = ReturnT.SUCCESS;
                            logger.debug(">>>>>>>>>>> xxl-job registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            break;
                        } else {
                            logger.info(">>>>>>>>>>> xxl-job registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                        }
                    } catch (Exception e) {
                        logger.error(">>>>>>>>>>> xxl-job registry error, registryParam:{}", registryParam, e);
                    }
                }
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> xxl-job registry error,", e);
            }
        };
        try {
            if (registryToStop.compareAndSet(false, true) && registryScheduledFuture == null) {
                registryScheduledFuture = executorService.scheduleAtFixedRate(registryThread, 5, RegistryConfig.BEAT_TIMEOUT, TimeUnit.SECONDS);
                logger.info(">>>>>>>>>>> xxl-job,Start JobRegistryMonitorHelper success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start JobRegistryMonitorHelper failed!", t);
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, executor registry thread  stopping");
            boolean hasAdminBizList = appName != null && appName.trim().length() > 0 && XxlJobExecutor.getAdminBizList() != null;
            if (hasAdminBizList) {
                try {
                    RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                    for (AdminBiz adminBiz : XxlJobExecutor.getAdminBizList()) {
                        try {
                            ReturnT<String> registryResult = adminBiz.registryRemove(registryParam);
                            if (registryResult != null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                                registryResult = ReturnT.SUCCESS;
                                logger.info(">>>>>>>>>>> xxl-job registry-remove success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                logger.info(">>>>>>>>>>> xxl-job registry-remove fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            }
                        } catch (Exception e) {
                            logger.error(">>>>>>>>>>> xxl-job registry-remove error, registryParam:{}", registryParam, e);

                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (registryToStop.compareAndSet(true, false) && registryScheduledFuture != null) {
                registryScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>> xxl-job,Stop executor registry thread  success!");
            }
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,stop  executor registry thread  failed!", t);
        }
    }

}
