package com.smartcoding.common.robot.model.request;

import com.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import com.smartcoding.common.robot.model.message.wx.news.WxNewsArticle;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * news图文请求
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:41 下午
 */
@Data
@Accessors(chain = true)
public class NewsRequest extends BaseNotifyRequest implements RobotNotifyRequest {
    private List<WxNewsArticle> articles = new ArrayList<WxNewsArticle>();

    public NewsRequest addArticle(WxNewsArticle wxNewsArticle) {
        this.articles.add(wxNewsArticle);
        return this;
    }

    public NewsRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.NEWS;
    }
}
