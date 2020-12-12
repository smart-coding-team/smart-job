package com.smartcoding.common.robot.model.request;

import com.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * link请求
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:39 下午
 */
@Data
@Accessors(chain = true)
public class LinkRequest extends BaseNotifyRequest implements RobotNotifyRequest {
    private String title;
    private String text;
    private String picUrl;
    private String messageUrl;

    public LinkRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.LINK;
    }
}
