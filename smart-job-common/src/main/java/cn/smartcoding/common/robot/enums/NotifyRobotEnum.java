package cn.smartcoding.common.robot.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 发送的服务类型
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:49 下午
 */
@Getter
public enum NotifyRobotEnum {
    /**
     * 企业微信机器人
     */
    CORP_WECHAT(0, "企业微信机器人", Arrays.asList(NotifyMsgTypeEnum.TEXT, NotifyMsgTypeEnum.MARKDOWN, NotifyMsgTypeEnum.LINK, NotifyMsgTypeEnum.NEWS, NotifyMsgTypeEnum.IMAGE)),
    /**
     * 钉钉机器人
     */
    DINGDING(1, "钉钉机器人", Arrays.asList(NotifyMsgTypeEnum.TEXT, NotifyMsgTypeEnum.MARKDOWN, NotifyMsgTypeEnum.LINK, NotifyMsgTypeEnum.FEED_CARD, NotifyMsgTypeEnum.ACTION_CARD));

    private int code;
    private String desc;
    /**
     * 支持的通知
     */
    private List<NotifyMsgTypeEnum> supports;

    NotifyRobotEnum(int code, String desc, List<NotifyMsgTypeEnum> supports) {
        this.code = code;
        this.desc = desc;
        this.supports = supports;
    }
}
