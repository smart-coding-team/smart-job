package com.smartcoding.common.robot.notify.dingding;

import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.model.message.dingding.*;
import com.smartcoding.common.robot.model.request.*;


/**
 * 钉钉机消息转换器
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:26 下午
 */
public class DingDingMessageCovert {

    public static Message buildMessage(RobotNotifyRequest robotNotifyRequest) {
        //文本消息
        if (robotNotifyRequest instanceof TextRequest) {
            TextRequest request = (TextRequest) robotNotifyRequest;
            DingTextMessage dingTextMessage = new DingTextMessage();
            dingTextMessage.setText(request.getText());
            dingTextMessage.setAtAll(request.isAtAll());
            dingTextMessage.setAtMobiles(request.getAtMobileList());
            return dingTextMessage;
        }
        //link消息
        if (robotNotifyRequest instanceof LinkRequest) {
            LinkRequest request = (LinkRequest) robotNotifyRequest;
            DingLinkMessage dingLinkMessage = new DingLinkMessage();
            dingLinkMessage.setTitle(request.getTitle());
            dingLinkMessage.setMessageUrl(request.getMessageUrl());
            dingLinkMessage.setPicUrl(request.getPicUrl());
            dingLinkMessage.setText(request.getText());
            return dingLinkMessage;
        }
        //markdown消息
        if (robotNotifyRequest instanceof MarkdownRequest) {
            MarkdownRequest request = (MarkdownRequest) robotNotifyRequest;
            DingMarkdownMessage dingMarkdownMessage = new DingMarkdownMessage();
            dingMarkdownMessage.setAtAll(request.isAtAll());
            dingMarkdownMessage.setTitle(request.getTitle());
            dingMarkdownMessage.setItems(request.getItems());
            dingMarkdownMessage.setAtMobileList(request.getAtMobileList());
            return dingMarkdownMessage;
        }
        //card消息
        if (robotNotifyRequest instanceof CardRequest) {
            CardRequest request = (CardRequest) robotNotifyRequest;
            DingFeedCardMessage cardMessage = new DingFeedCardMessage();
            cardMessage.setLinks(request.getLinks());
            return cardMessage;
        }
        //固定actionCard
        if (robotNotifyRequest instanceof SingleActionCardRequest) {
            SingleActionCardRequest request = (SingleActionCardRequest) robotNotifyRequest;
            DingSingleActionCardMessage dingSingleActionCardMessage = new DingSingleActionCardMessage();
            dingSingleActionCardMessage.setTitle(request.getTitle());
            dingSingleActionCardMessage.setText(request.getText());
            dingSingleActionCardMessage.setSingleTitle(request.getSingleTitle());
            dingSingleActionCardMessage.setSingleURL(request.getSingleURL());
            dingSingleActionCardMessage.setBtnOrientation(request.getBtnOrientation());
            dingSingleActionCardMessage.setHideAvatar(request.getHideAvatar());
            return dingSingleActionCardMessage;
        }
        //固定actionCard
        if (robotNotifyRequest instanceof ActionCardRequest) {
            ActionCardRequest request = (ActionCardRequest) robotNotifyRequest;
            DingActionCardMessage dingActionCardMessage = new DingActionCardMessage();
            dingActionCardMessage.setTitle(request.getTitle());
            dingActionCardMessage.setText(request.getText());
            dingActionCardMessage.setBtnOrientation(request.getBtnOrientation());
            dingActionCardMessage.setHideAvatar(request.getHideAvatar());
            dingActionCardMessage.setBtns(request.getBtns());
            return dingActionCardMessage;
        }
        return null;
    }
}
