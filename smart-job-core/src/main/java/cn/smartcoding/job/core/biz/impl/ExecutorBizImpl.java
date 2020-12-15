package cn.smartcoding.job.core.biz.impl;

import cn.smartcoding.job.core.biz.model.LogResult;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.biz.model.TriggerParam;
import cn.smartcoding.job.core.enums.ExecutorBlockStrategyEnum;
import cn.smartcoding.job.core.executor.XxlJobExecutor;
import cn.smartcoding.job.core.glue.GlueFactory;
import cn.smartcoding.job.core.glue.GlueTypeEnum;
import cn.smartcoding.job.core.handler.IJobHandler;
import cn.smartcoding.job.core.biz.ExecutorBiz;
import cn.smartcoding.job.core.handler.impl.GlueJobHandler;
import cn.smartcoding.job.core.handler.impl.ScriptJobHandler;
import cn.smartcoding.job.core.log.XxlJobFileAppender;
import cn.smartcoding.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by xuxueli on 17/3/1.
 */
public class ExecutorBizImpl implements ExecutorBiz {
    private static Logger logger = LoggerFactory.getLogger(ExecutorBizImpl.class);

    @Override
    public ReturnT<String> beat() {
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> idleBeat(Long jobId) {

        // isRunningOrHasQueue
        boolean isRunningOrHasQueue = false;
        JobThread jobThread = XxlJobExecutor.loadJobThread(jobId);
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }
        if (isRunningOrHasQueue) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "job thread is running or has trigger queue.");
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> kill(Long jobId) {
        // kill handlerThread, and create new one
        JobThread jobThread = XxlJobExecutor.loadJobThread(jobId);
        if (jobThread != null) {
            XxlJobExecutor.removeJobThread(jobId, "scheduling center kill job.");
            return ReturnT.SUCCESS;
        }

        return new ReturnT<String>(ReturnT.SUCCESS_CODE, "job thread already killed.");
    }

    @Override
    public ReturnT<LogResult> log(long logDateTime, Long logId, int fromLineNum) {
        // log filename: logPath/yyyy-MM-dd/9999.log
        String logFileName = XxlJobFileAppender.makeLogFileName(new Date(logDateTime), logId);

        LogResult logResult = XxlJobFileAppender.readLog(logFileName, fromLineNum);
        return new ReturnT<LogResult>(logResult);
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        // load old：jobHandler + jobThread
        JobThread jobThread = XxlJobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread != null ? jobThread.getHandler() : null;
        String removeOldReason = null;

        // valid：jobHandler + jobThread
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(triggerParam.getGlueType());
        if (GlueTypeEnum.BEAN == glueTypeEnum) {
            // new jobhandler
            IJobHandler newJobHandler = XxlJobExecutor.loadJobHandler(triggerParam.getExecutorHandler());

            // valid old jobThread
            if (jobThread != null && jobHandler != newJobHandler) {
                // change handler, need kill old thread
                removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

                jobThread = null;
                jobHandler = null;
            }

            // valid handler
            if (jobHandler == null) {
                jobHandler = newJobHandler;
                if (jobHandler == null) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
                }
            }

        } else {
            Long glueUpdateTime = triggerParam.getGlueUpdateTime();
            if (GlueTypeEnum.GLUE_GROOVY == glueTypeEnum) {

                // valid old jobThread
                if (jobThread != null && !(jobThread.getHandler() instanceof GlueJobHandler
                        && ((GlueJobHandler) jobThread.getHandler()).getGlueUpdateTime().equals(glueUpdateTime))) {
                    // change handler or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    try {
                        IJobHandler originJobHandler = GlueFactory.getInstance().loadNewInstance(triggerParam.getGlueSource());
                        jobHandler = new GlueJobHandler(originJobHandler, glueUpdateTime);
                    } catch (Exception e) {
                        logger.error("init GlueJobHandler error", e);
                        return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
                    }
                }
            } else if (glueTypeEnum != null && glueTypeEnum.isScript()) {

                // valid old jobThread
                if (jobThread != null && !(jobThread.getHandler() instanceof ScriptJobHandler
                        && ((ScriptJobHandler) jobThread.getHandler()).getGlueUpdatetime().equals(glueUpdateTime))) {
                    // change script or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    jobHandler = new ScriptJobHandler(triggerParam.getJobId(), glueUpdateTime, triggerParam.getGlueSource(), GlueTypeEnum.match(triggerParam.getGlueType()));
                }
            } else {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "glueType[" + triggerParam.getGlueType() + "] is not valid.");
            }
        }

        // executor block strategy
        if (jobThread != null) {
            ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(triggerParam.getExecutorBlockStrategy(), null);
            if (ExecutorBlockStrategyEnum.DISCARD_LATER == blockStrategy) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "block strategy effect：" + ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                }
            } else if (ExecutorBlockStrategyEnum.COVER_EARLY == blockStrategy) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason = "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();
                    jobThread = null;
                }
            } else {
                // just queue trigger
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = XxlJobExecutor.registJobThread(triggerParam.getJobId(), triggerParam.getJobRequestId(), jobHandler, removeOldReason);
        }

        // push data to queue
        ReturnT<String> pushResult = jobThread.pushTriggerQueue(triggerParam);
        return pushResult;
    }

}
