package cn.smartcoding.common.robot.model.request;

import cn.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import cn.smartcoding.common.robot.enums.NotifyRobotEnum;

public interface RobotNotifyRequest {

    NotifyMsgTypeEnum getMsgType();

    String getWebHook();

    void setWebHook(String webHook);

    NotifyRobotEnum getNotifyRobotEnum();

    void setNotifyRobotEnum(NotifyRobotEnum notifyRobotEnum);
}
