

package com.smartcoding.schedule.core.route.strategy;

import cn.hutool.core.util.StrUtil;
import com.smartcoding.schedule.core.conf.XxlJobScheduler;
import com.smartcoding.schedule.core.route.ExecutorRouter;
import com.smartcoding.job.core.biz.ExecutorBiz;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        for (String address : addressList) {
            // beat
            ReturnT<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = new ReturnT<String>(ReturnT.FAIL_CODE, "" + e);
            }
            IdleBeat idleBeat = new IdleBeat();
            idleBeat.setTitle("jobconf_idleBeat");
            idleBeat.setAddress(address);
            idleBeat.setMsg(StrUtil.nullToEmpty(idleBeatResult.getMsg()));
            idleBeat.setCode(idleBeatResult.getCode());
            // beat success
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
                idleBeatResult.setMsg(idleBeat.toJson());
                idleBeatResult.setContent(address);
                return idleBeatResult;
            }
        }

        return new ReturnT<String>(ReturnT.FAIL_CODE, null);
    }

}
