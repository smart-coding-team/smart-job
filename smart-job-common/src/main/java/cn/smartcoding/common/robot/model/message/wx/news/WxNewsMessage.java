package cn.smartcoding.common.robot.model.message.wx.news;

import cn.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import cn.smartcoding.common.robot.model.message.Message;
import cn.smartcoding.common.robot.util.ErrorStringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图文消息
 *
 * @author wuque
 */
@Setter
@Getter
public class WxNewsMessage implements Message {
    public static final int MAX_ARTICLE_CNT = 5;
    public static final int MIN_ARTICLE_CNT = 1;

    private List<WxNewsArticle> articles = new ArrayList<WxNewsArticle>();

    public void addNewsArticle(WxNewsArticle wxNewsArticle) {
        if (articles.size() >= MAX_ARTICLE_CNT) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.wxError("number of articles can't more than ") + MAX_ARTICLE_CNT);
        }
        articles.add(wxNewsArticle);
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "news");

        Map<String, Object> news = new HashMap<String, Object>();
        if (articles.size() < MIN_ARTICLE_CNT) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.wxError("number of articles can't less than ") + MIN_ARTICLE_CNT);
        }
        news.put("articles", articles);
        items.put("news", news);
        return JSON.toJSONString(items);
    }
}
