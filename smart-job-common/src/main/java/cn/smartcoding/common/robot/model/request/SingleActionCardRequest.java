package cn.smartcoding.common.robot.model.request;

import cn.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SingleActionCardRequest extends BaseNotifyRequest implements RobotNotifyRequest {

    private String title;
    private String text;
    private String singleTitle;
    private String singleURL;
    private String btnOrientation;
    private String hideAvatar;

    public SingleActionCardRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.ACTION_CARD;
    }
}
