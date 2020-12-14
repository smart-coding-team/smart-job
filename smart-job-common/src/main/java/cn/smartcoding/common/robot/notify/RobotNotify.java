package cn.smartcoding.common.robot.notify;


import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import cn.smartcoding.common.robot.model.request.RobotNotifyRequest;
import cn.smartcoding.common.robot.model.response.RobotNotifyResponse;
import cn.smartcoding.common.robot.notify.corpwechat.WechatRobotNotify;
import cn.smartcoding.common.robot.notify.dingding.DingDingRobotNotify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 通知接口
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:06 下午
 */
public interface RobotNotify {


    /**
     * 同步发送机器人通知
     *
     * @param messageRequest 通知消息
     * @return 发送结果
     */
    RobotNotifyResponse sendRobotSyncNotify(RobotNotifyRequest messageRequest);

    /**
     * 异步发送机器人通知
     *
     * @param messageRequest 通知消息
     * @return 发送结果
     */
    RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest messageRequest);

    /**
     * 异步发送机器人通知,使用自己的线程池
     *
     * @param messageRequest 通知消息
     * @return 发送结果
     */
    RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest messageRequest, Executor executor);

    /**
     * 默认方法
     *
     * @return
     */
    static RobotNotify defaultRobotNotify() {
        if (BaseRobotNotify.ROBOT_NOTIFY != null) {
            return BaseRobotNotify.ROBOT_NOTIFY;
        }
        synchronized (BaseRobotNotify.class) {
            if (null == BaseRobotNotify.ROBOT_NOTIFY) {
                BaseRobotNotify robotNotify = new BaseRobotNotify();
                Map<Integer, AbstractRobotNotify> robotNotifyMap = new HashMap<>(2);
                robotNotifyMap.put(NotifyRobotEnum.CORP_WECHAT.getCode(), new WechatRobotNotify());
                robotNotifyMap.put(NotifyRobotEnum.DINGDING.getCode(), new DingDingRobotNotify());
                robotNotify.setRobotNotifyMap(robotNotifyMap);
                BaseRobotNotify.ROBOT_NOTIFY = robotNotify;
            }
        }
        return BaseRobotNotify.ROBOT_NOTIFY;
    }

}
