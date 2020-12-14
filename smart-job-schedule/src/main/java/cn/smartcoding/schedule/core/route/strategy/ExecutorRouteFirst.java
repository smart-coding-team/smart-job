

package cn.smartcoding.schedule.core.route.strategy;

import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.biz.model.TriggerParam;
import cn.smartcoding.schedule.core.route.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList){
        return new ReturnT<String>(addressList.get(0));
    }

}
