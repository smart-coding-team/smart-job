package cn.smartcoding.job.core.biz;

import cn.smartcoding.job.core.biz.model.LogResult;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.biz.model.TriggerParam;

/**
 * 任务执行器
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     *
     * @return  result
     */
    ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param jobId
     * @return result
     */
    ReturnT<String> idleBeat(Long jobId);

    /**
     * kill
     *
     * @param jobId
     * @return result
     */
    ReturnT<String> kill(Long jobId);

    /**
     * log
     *
     * @param logDateTime   start time
     * @param logId    jobLogId
     * @param fromLineNum  from line num
     * @return  result
     */
    ReturnT<LogResult> log(long logDateTime, Long logId, int fromLineNum);

    /**
     * run
     *
     * @param triggerParam  trigger
     * @return  result
     */
    ReturnT<String> run(TriggerParam triggerParam);

}
