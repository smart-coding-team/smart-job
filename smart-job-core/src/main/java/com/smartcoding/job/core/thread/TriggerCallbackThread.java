package com.smartcoding.job.core.thread;

import com.smartcoding.job.core.biz.AdminBiz;
import com.smartcoding.job.core.biz.model.HandleCallbackParam;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.enums.RegistryConfig;
import com.smartcoding.job.core.executor.XxlJobExecutor;
import com.smartcoding.job.core.log.XxlJobFileAppender;
import com.smartcoding.job.core.log.XxlJobLogger;
import com.smartcoding.job.core.util.FileUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xuxueli on 16/7/22.
 */
public class TriggerCallbackThread {
    private static Logger logger = LoggerFactory.getLogger(TriggerCallbackThread.class);

    private static TriggerCallbackThread instance = new TriggerCallbackThread();

    public static TriggerCallbackThread getInstance() {
        return instance;
    }

    /**
     * job results callback queue
     */
    private LinkedBlockingQueue<HandleCallbackParam> callBackQueue = new LinkedBlockingQueue<HandleCallbackParam>();

    public static void pushCallBack(HandleCallbackParam callback) {
        getInstance().callBackQueue.add(callback);
        logger.debug(">>>>>>>>>>> xxl-job, push callback request, logId:{}", callback.getLogId());
    }

    /**
     * callback thread
     */
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new BasicThreadFactory.Builder().namingPattern("JobScheduleHelper").daemon(true).build());
    private ScheduledFuture<?> scheduleThreadScheduledFuture;
    private ScheduledFuture<?> ringThreadScheduledFuture;
    private AtomicBoolean triggerCallbackThreadToStop = new AtomicBoolean(false);

    public void start() {
        // callback
        Runnable triggerCallbackThread = () -> {
            // valid
            if (XxlJobExecutor.getAdminBizList() == null) {
                logger.warn(">>>>>>>>>>> xxl-job, executor callback config fail, adminAddresses is null.");
                return;
            }
            try {
                HandleCallbackParam callback = getInstance().callBackQueue.take();
                if (callback != null) {
                    // callback list param
                    List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                    int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                    callbackParamList.add(callback);
                    // callback, will retry if error
                    if (callbackParamList.size() > 0) {
                        doCallback(callbackParamList);
                    }
                }
            } catch (InterruptedException e) {
                logger.warn(">>>>>>>>>>> xxl-job, executor callback config fail, triggerCallbackThread stopping.");
            } catch (Exception e) {
                logger.error("normal callback error", e);
            }

        };
        // retry
        Runnable triggerRetryCallbackThread = () -> {

            // valid
            if (XxlJobExecutor.getAdminBizList() == null) {
                logger.warn(">>>>>>>>>>> xxl-job, executor callback config fail, adminAddresses is null.");
                return;
            }
            try {
                retryFailCallbackFile();
            } catch (Exception e) {
                logger.error("retryFailCallbackFile callback error", e);
            }
        };
        try {
            if (triggerCallbackThreadToStop.compareAndSet(false, true)) {
                if (scheduleThreadScheduledFuture == null) {
                    scheduleThreadScheduledFuture = executorService.scheduleAtFixedRate(triggerCallbackThread, 1000, 1000, TimeUnit.MILLISECONDS);
                    logger.info(">>>>>>>>>>> xxl-job,Start TriggerCallbackThread success!");
                }
                if (ringThreadScheduledFuture == null) {
                    ringThreadScheduledFuture = executorService.scheduleAtFixedRate(triggerRetryCallbackThread, 1, RegistryConfig.BEAT_TIMEOUT, TimeUnit.SECONDS);
                    logger.info(">>>>>>>>>>> xxl-job,Start triggerRetryCallbackThread   success!");
                }
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start TriggerCallbackThread failed!", t);
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, triggerCallbackThread stop");
            // last callback
            try {
                List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                if (callbackParamList.size() > 0) {
                    doCallback(callbackParamList);
                }
            } catch (Exception e) {
                logger.error("last callback error", e);
            }
            if (triggerCallbackThreadToStop.compareAndSet(true, false)) {
                if (scheduleThreadScheduledFuture != null) {
                    scheduleThreadScheduledFuture.cancel(true);
                }
                if (ringThreadScheduledFuture != null) {
                    ringThreadScheduledFuture.cancel(true);
                }
                logger.info(">>>>>>>>>>> xxl-job, Stop TriggerCallbackThread  success!");
            }
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job, stop TriggerCallbackThread   failed!", t);
        }
    }

    /**
     * do callback, will retry if error
     *
     * @param callbackParamList
     */
    private void doCallback(List<HandleCallbackParam> callbackParamList) {
        boolean callbackRet = false;
        // callback, will retry if error
        for (AdminBiz adminBiz : XxlJobExecutor.getAdminBizList()) {
            try {
                ReturnT<String> callbackResult = adminBiz.callback(callbackParamList);
                if (callbackResult != null && ReturnT.SUCCESS_CODE == callbackResult.getCode()) {
                    callbackLog(callbackParamList, "<br>----------- xxl-job job callback finish.");
                    callbackRet = true;
                    break;
                } else {
                    callbackLog(callbackParamList, "<br>----------- xxl-job job callback fail, callbackResult:" + callbackResult);
                }
            } catch (Exception e) {
                callbackLog(callbackParamList, "<br>----------- xxl-job job callback error, errorMsg:" + e.getMessage());
            }
        }
        if (!callbackRet) {
            appendFailCallbackFile(callbackParamList);
        }
    }

    /**
     * callback log
     */
    private void callbackLog(List<HandleCallbackParam> callbackParamList, String logContent) {
        for (HandleCallbackParam callbackParam : callbackParamList) {
            String logFileName = XxlJobFileAppender.makeLogFileName(new Date(callbackParam.getLogDateTim()), callbackParam.getLogId());
            XxlJobFileAppender.contextHolder.set(logFileName);
            XxlJobLogger.log(logContent);
        }
    }


    // ---------------------- fail-callback file ----------------------

    private static String failCallbackFilePath = XxlJobFileAppender.getLogPath().concat(File.separator).concat("callbacklog").concat(File.separator);
    private static String failCallbackFileName = failCallbackFilePath.concat("xxl-job-callback-{x}").concat(".log");

    private void appendFailCallbackFile(List<HandleCallbackParam> callbackParamList) {
        // valid
        if (callbackParamList == null || callbackParamList.size() == 0) {
            return;
        }

        // append file
        byte[] callbackParamListBytes = XxlJobExecutor.getSerializer().serialize(callbackParamList);

        File callbackLogFile = new File(failCallbackFileName.replace("{x}", String.valueOf(System.currentTimeMillis())));
        if (callbackLogFile.exists()) {
            for (int i = 0; i < 100; i++) {
                callbackLogFile = new File(failCallbackFileName.replace("{x}", String.valueOf(System.currentTimeMillis()).concat("-").concat(String.valueOf(i))));
                if (!callbackLogFile.exists()) {
                    break;
                }
            }
        }
        FileUtil.writeFileContent(callbackLogFile, callbackParamListBytes);
    }

    private void retryFailCallbackFile() {

        // valid
        File callbackLogPath = new File(failCallbackFilePath);
        if (!callbackLogPath.exists()) {
            return;
        }
        if (callbackLogPath.isFile()) {
            callbackLogPath.delete();
        }
        if (!(callbackLogPath.isDirectory() && callbackLogPath.list() != null && Objects.requireNonNull(callbackLogPath.list()).length > 0)) {
            return;
        }

        // load and clear file, retry
        File[] files = callbackLogPath.listFiles();
        if (files == null) {
            return;
        }
        for (File callbackLogFile : files) {
            byte[] callbackParamListBytes = FileUtil.readFileContent(callbackLogFile);
            List<HandleCallbackParam> callbackParamList = (List<HandleCallbackParam>) XxlJobExecutor.getSerializer().deserialize(callbackParamListBytes, HandleCallbackParam.class);
            boolean delete = callbackLogFile.delete();
            doCallback(callbackParamList);
        }

    }

}
