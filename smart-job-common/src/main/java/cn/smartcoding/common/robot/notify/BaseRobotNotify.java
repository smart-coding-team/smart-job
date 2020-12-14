package cn.smartcoding.common.robot.notify;


import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import cn.smartcoding.common.robot.exception.WebHookRobotValidateException;
import cn.smartcoding.common.robot.model.request.RobotNotifyRequest;
import cn.smartcoding.common.robot.model.response.RobotNotifyResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 通知整合service
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:09 下午
 */
@Slf4j
public class BaseRobotNotify implements RobotNotify {

    public static BaseRobotNotify ROBOT_NOTIFY = null;

    private Map<Integer, AbstractRobotNotify> robotNotifyMap = new HashMap<>();

    private void checkRequest(RobotNotifyRequest robotNotifyRequest) {
        if (null == robotNotifyRequest) {
            throw new WebHookRobotValidateException("通知请求不能为空!");
        }
    }

    @Override
    public RobotNotifyResponse sendRobotSyncNotify(RobotNotifyRequest robotNotifyRequest) {
        checkRequest(robotNotifyRequest);
        NotifyRobotEnum notifyRobotEnum = robotNotifyRequest.getNotifyRobotEnum();
        AbstractRobotNotify abstractRobotNotify = robotNotifyMap.get(notifyRobotEnum.getCode());
        if (abstractRobotNotify != null) {
            return abstractRobotNotify.sendRobotSyncNotify(robotNotifyRequest);

        }
        return RobotNotifyResponse.error("没有对应的通知机器人类型!");

    }

    @Override
    public RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest robotNotifyRequest) {
        checkRequest(robotNotifyRequest);
        NotifyRobotEnum notifyRobotEnum = robotNotifyRequest.getNotifyRobotEnum();
        AbstractRobotNotify abstractRobotNotify = robotNotifyMap.get(notifyRobotEnum.getCode());
        if (abstractRobotNotify != null) {
            return abstractRobotNotify.sendRobotAsyncNotify(robotNotifyRequest);
        }
        return RobotNotifyResponse.error("没有对应的通知机器人类型!");
    }

    @Override
    public RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest robotNotifyRequest, Executor executor) {
        checkRequest(robotNotifyRequest);
        NotifyRobotEnum notifyRobotEnum = robotNotifyRequest.getNotifyRobotEnum();
        AbstractRobotNotify abstractRobotNotify = robotNotifyMap.get(notifyRobotEnum.getCode());
        if (abstractRobotNotify != null) {
            return abstractRobotNotify.sendRobotAsyncNotify(robotNotifyRequest, executor);
        }
        return RobotNotifyResponse.error("没有对应的通知机器人类型!");
    }

    public void setRobotNotifyMap(Map<Integer, AbstractRobotNotify> robotNotifyMap) {
        this.robotNotifyMap = robotNotifyMap;
    }
}
