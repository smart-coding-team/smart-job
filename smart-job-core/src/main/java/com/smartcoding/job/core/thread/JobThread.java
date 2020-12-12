package com.smartcoding.job.core.thread;

import com.smartcoding.job.core.biz.model.HandleCallbackParam;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.biz.model.TriggerParam;
import com.smartcoding.job.core.executor.XxlJobExecutor;
import com.smartcoding.job.core.handler.IJobHandler;
import com.smartcoding.job.core.log.XxlJobFileAppender;
import com.smartcoding.job.core.log.XxlJobLogger;
import com.smartcoding.job.core.util.ShardingUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;


/**
 * handler thread
 *
 * @author xuxueli 2016-1-16 19:52:47
 */
public class JobThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(JobThread.class);

    private Long jobId;
    private IJobHandler handler;
    private LinkedBlockingQueue<TriggerParam> triggerQueue;
    // avoid repeat trigger for the same TRIGGER_LOG_ID
    private Set<Long> triggerLogIdSet;

    private volatile boolean toStop = false;
    private String stopReason;
    // if running job
    private boolean running = false;
    // idel times
    private int idleTimes = 0;


    public JobThread(Long jobId, IJobHandler handler) {
        this.jobId = jobId;
        this.handler = handler;
        this.triggerQueue = new LinkedBlockingQueue<TriggerParam>();
        this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<Long>());
    }

    public IJobHandler getHandler() {
        return handler;
    }

    /**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
    public ReturnT<String> pushTriggerQueue(TriggerParam triggerParam) {
        // avoid repeat
        if (triggerLogIdSet.contains(triggerParam.getLogId())) {
            logger.info(">>>>>>>>>>> repeat trigger job, logId:{}", triggerParam.getLogId());
            return new ReturnT<String>(ReturnT.FAIL_CODE, "repeat trigger job, logId:" + triggerParam.getLogId());
        }

        triggerLogIdSet.add(triggerParam.getLogId());
        triggerQueue.add(triggerParam);
        return ReturnT.SUCCESS;
    }

    /**
     * kill job thread
     *
     * @param stopReason
     */
    public void toStop(String stopReason) {
        /**
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
         * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
         * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
         */
        this.toStop = true;
        this.stopReason = stopReason;
    }

    /**
     * is running job
     *
     * @return
     */
    public boolean isRunningOrHasQueue() {
        return running || triggerQueue.size() > 0;
    }

    @Override
    public void run() {

        // init
        try {
            handler.init();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        // execute
        while (!toStop) {
            running = false;
            idleTimes++;

            TriggerParam triggerParam = null;
            ReturnT<String> executeResult = null;
            String requestId = "requestId";
            try {
                // to check toStop signal, we need cycle, so wo cannot use queue.take(), instand of poll(timeout)
                triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) {
                    running = true;
                    idleTimes = 0;
                    triggerLogIdSet.remove(triggerParam.getLogId());

                    // log filename, like "logPath/yyyy-MM-dd/9999.log"
                    String logFileName = XxlJobFileAppender.makeLogFileName(new Date(triggerParam.getLogDateTim()), triggerParam.getLogId());
                    XxlJobFileAppender.contextHolder.set(logFileName);
                    ShardingUtil.setShardingVo(new ShardingUtil.ShardingVO(triggerParam.getBroadcastIndex(), triggerParam.getBroadcastTotal()));

                    // execute
                    XxlJobLogger.log("<br>----------- xxl-job job execute start -----------<br>----------- Param:" + triggerParam.getExecutorParams());
                    final String jobRequestId = triggerParam.getJobRequestId();
                    requestId = StringUtils.isEmpty(triggerParam.getRequestId()) ? "requestId" : triggerParam.getRequestId();
                    if (triggerParam.getExecutorTimeout() > 0) {
                        // limit timeout
                        Thread futureThread = null;
                        try {
                            final TriggerParam triggerParamTmp = triggerParam;

                            String finalRequestId = requestId;
                            FutureTask<ReturnT<String>> futureTask = new FutureTask<ReturnT<String>>(new Callable<ReturnT<String>>() {
                                @Override
                                public ReturnT<String> call() throws Exception {
                                    MDC.put(finalRequestId, jobRequestId);
                                    return handler.execute(triggerParamTmp.getExecutorParams());
                                }
                            });
                            futureThread = new Thread(futureTask);
                            futureThread.start();

                            executeResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {

                            XxlJobLogger.log("<br>----------- xxl-job job execute timeout");
                            XxlJobLogger.log(e);

                            executeResult = new ReturnT<String>(IJobHandler.FAIL_TIMEOUT.getCode(), "job execute timeout ");
                        } finally {
                            futureThread.interrupt();
                        }
                    } else {
                        // just execute
                        MDC.put(requestId, jobRequestId);
                        executeResult = handler.execute(triggerParam.getExecutorParams());
                    }

                    if (executeResult == null) {
                        executeResult = IJobHandler.FAIL;
                    } else {
                        String msg = (executeResult != null && executeResult.getMsg() != null && executeResult.getMsg().length() > 50000)
                                ? executeResult.getMsg().substring(0, 50000).concat("...")
                                : executeResult.getMsg();
                        executeResult.setMsg(msg);
                        executeResult.setContent(null);    // limit obj size
                    }
                    XxlJobLogger.log("<br>----------- xxl-job job execute end(finish) -----------<br>----------- ReturnT:" + executeResult);

                } else {
                    if (idleTimes > 30) {
                        if (triggerQueue.size() == 0) {    // avoid concurrent trigger causes jobId-lost
                            XxlJobExecutor.removeJobThread(jobId, "excutor idel times over limit.");
                        }
                    }
                }
            } catch (Throwable e) {
                if (toStop) {
                    XxlJobLogger.log("<br>----------- JobThread toStop, stopReason:" + stopReason);
                }

                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                String errorMsg = stringWriter.toString();
                executeResult = new ReturnT<String>(ReturnT.FAIL_CODE, errorMsg);

                XxlJobLogger.log("<br>----------- JobThread Exception:" + errorMsg + "<br>----------- xxl-job job execute end(error) -----------");
            } finally {
                MDC.remove(requestId);
                if (triggerParam != null) {
                    // callback handler info
                    if (!toStop) {
                        // commonm
                        TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTim(), executeResult));
                    } else {
                        // is killed
                        ReturnT<String> stopResult = new ReturnT<String>(ReturnT.FAIL_CODE, stopReason + " [job running，killed]");
                        TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTim(), stopResult));
                    }
                }
            }
        }

        // callback trigger request in queue
        while (triggerQueue != null && triggerQueue.size() > 0) {
            TriggerParam triggerParam = triggerQueue.poll();
            if (triggerParam != null) {
                // is killed
                ReturnT<String> stopResult = new ReturnT<String>(ReturnT.FAIL_CODE, stopReason + " [job not executed, in the job queue, killed.]");
                TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTim(), stopResult));
            }
        }

        // destroy
        try {
            handler.destroy();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        logger.info(">>>>>>>>>>> xxl-job JobThread stoped, hashCode:{}", Thread.currentThread());
    }
}
