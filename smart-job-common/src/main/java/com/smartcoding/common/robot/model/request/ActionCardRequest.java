package com.smartcoding.common.robot.model.request;

import com.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import com.smartcoding.common.robot.model.message.dingding.DingActionCardMessage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ActionCardRequest extends BaseNotifyRequest implements RobotNotifyRequest {

    private String title;
    private String text;
    private String hideAvatar;
    private String btnOrientation;
    private List<DingActionCardMessage.Btn> btns;

    public ActionCardRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.ACTION_CARD;
    }
}
