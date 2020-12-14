package cn.smartcoding.common.robot.model.response;

import cn.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import cn.smartcoding.common.robot.exception.WebHookRobotValidateException;
import cn.smartcoding.common.robot.model.request.*;

/**
 * 创建通知请求
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 11:59 上午
 */
public class RobotNotifyRequestBuilder {

    private static void checkNotifyType(NotifyRobotEnum notifyRobotEnum, NotifyMsgTypeEnum notifyMsgTypeEnum) {
        if (null == notifyRobotEnum) {
            throw new WebHookRobotValidateException("通知机器类型不能为空");
        }
        if (!notifyRobotEnum.getSupports().contains(notifyMsgTypeEnum)) {
            throw new WebHookRobotValidateException(notifyRobotEnum.getDesc() + "不支持[" + notifyMsgTypeEnum + "]消息类型");
        }
    }

    public static TextRequest textMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.TEXT);
        return new TextRequest(webHook, notifyRobotEnum);
    }

    public static MarkdownRequest markdownMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.MARKDOWN);
        return new MarkdownRequest(webHook, notifyRobotEnum);
    }

    public static LinkRequest linkMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.LINK);
        return new LinkRequest(webHook, notifyRobotEnum);
    }

    /**
     * 微信特有
     *
     * @param webHook
     * @param notifyRobotEnum
     * @return
     */
    public static NewsRequest newsMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.NEWS);
        return new NewsRequest(webHook, notifyRobotEnum);
    }

    public static ImageRequest imageMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.IMAGE);
        return new ImageRequest(webHook, notifyRobotEnum);
    }

    /**
     * 钉钉特有
     *
     * @param webHook
     * @param notifyRobotEnum
     * @return
     */
    public static CardRequest cardMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.FEED_CARD);
        return new CardRequest(webHook, notifyRobotEnum);
    }

    public static SingleActionCardRequest singleActionCardMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.ACTION_CARD);
        return new SingleActionCardRequest(webHook, notifyRobotEnum);
    }

    public static ActionCardRequest actionCardMessageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        checkNotifyType(notifyRobotEnum, NotifyMsgTypeEnum.ACTION_CARD);
        return new ActionCardRequest(webHook, notifyRobotEnum);
    }

}
