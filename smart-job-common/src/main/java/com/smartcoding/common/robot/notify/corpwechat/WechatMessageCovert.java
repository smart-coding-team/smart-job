package com.smartcoding.common.robot.notify.corpwechat;

import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.model.message.wx.WxImageMessage;
import com.smartcoding.common.robot.model.message.wx.WxMarkdownMessage;
import com.smartcoding.common.robot.model.message.wx.WxTextMessage;
import com.smartcoding.common.robot.model.message.wx.news.WxNewsArticle;
import com.smartcoding.common.robot.model.message.wx.news.WxNewsMessage;
import com.smartcoding.common.robot.model.request.*;

/**
 * 企业微信消息转换器
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:26 下午
 */
public class WechatMessageCovert {


    public static Message buildMessage(RobotNotifyRequest robotNotifyRequest) {
        //文本消息
        if (robotNotifyRequest instanceof TextRequest) {
            TextRequest request = (TextRequest) robotNotifyRequest;
            WxTextMessage wxTextMessage = new WxTextMessage();
            wxTextMessage.setText(request.getText());
            wxTextMessage.setAtAll(request.isAtAll());
            wxTextMessage.setMentionedMobileList(request.getAtMobileList());
            wxTextMessage.setMentionedList(request.getAtUserIdList());
            return wxTextMessage;
        }
        //markdown消息
        if (robotNotifyRequest instanceof MarkdownRequest) {
            MarkdownRequest request = (MarkdownRequest) robotNotifyRequest;
            WxMarkdownMessage wxMarkdownMessage = new WxMarkdownMessage();
            wxMarkdownMessage.setItems(request.getItems());
            return wxMarkdownMessage;
        }
        //图文消息
        if (robotNotifyRequest instanceof NewsRequest) {
            NewsRequest request = (NewsRequest) robotNotifyRequest;
            WxNewsMessage wxNewsMessage = new WxNewsMessage();
            wxNewsMessage.setArticles(request.getArticles());
            return wxNewsMessage;
        }
        //图片消息
        if (robotNotifyRequest instanceof ImageRequest) {
            ImageRequest request = (ImageRequest) robotNotifyRequest;
            WxImageMessage message = new WxImageMessage();
            message.setBase64(request.getBase64());
            message.setMd5(request.getMd5());
            return message;
        }
        //连接消息
        if (robotNotifyRequest instanceof LinkRequest) {
            LinkRequest request = (LinkRequest) robotNotifyRequest;
            WxNewsMessage wxNewsMessage = new WxNewsMessage();
            wxNewsMessage.addNewsArticle(
                    WxNewsArticle.builder()
                            .title(request.getTitle())
                            .description(request.getText())
                            .picurl(request.getPicUrl())
                            .url(request.getMessageUrl())
                            .build()
            );
            return wxNewsMessage;
        }
        return null;
    }
}
