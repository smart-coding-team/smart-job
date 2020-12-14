package cn.smartcoding.common.robot.enums;

/**
 * 响应的消息类型
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:51 下午
 */
public enum NotifyMsgTypeEnum {
    /**
     * text消息
     */
    TEXT,
    /**
     * markdown消息
     */
    MARKDOWN,
    /**
     * link消息
     */
    LINK,
    /**
     * IMAGE图片消息
     */
    IMAGE,
    /**
     * news图文消息
     */
    NEWS,
    /**
     * feedCard消息
     */
    FEED_CARD,

    /**
     * actionCard 图文消息
     */
    ACTION_CARD;
}
