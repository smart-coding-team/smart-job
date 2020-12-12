package com.smartcoding.common.robot.model.request;

import com.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 文本请求
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:37 下午
 */
@Data
@Accessors(chain = true)
public class TextRequest extends BaseNotifyRequest implements RobotNotifyRequest {
    /**
     * 消息的文本
     */
    private String text;
    /**
     * 需要at @ 的人：手机号码
     */
    private List<String> atMobileList;
    /**
     * 需要at @ 的人：userid
     */
    private List<String> atUserIdList;
    /**
     * @all 所以人
     */
    private boolean isAtAll;

    public TextRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.TEXT;
    }
}
