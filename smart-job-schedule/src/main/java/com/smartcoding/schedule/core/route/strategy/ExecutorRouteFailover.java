

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
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {

        for (String address : addressList) {
            // beat
            ReturnT<String> beatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                beatResult = new ReturnT<String>(ReturnT.FAIL_CODE, "" + e);
            }
            IdleBeat idleBeat = new IdleBeat();
            idleBeat.setAddress(address);
            idleBeat.setCode(beatResult.getCode());
            idleBeat.setTitle("jobconf_beat");
            idleBeat.setMsg(StrUtil.nullToEmpty(beatResult.getMsg()));
            // beat success
            if (beatResult.getCode() == ReturnT.SUCCESS_CODE) {
                beatResult.setMsg(idleBeat.toJson());
                beatResult.setContent(address);
                return beatResult;
            }
        }
        return new ReturnT<String>(ReturnT.FAIL_CODE, null);

    }
}
