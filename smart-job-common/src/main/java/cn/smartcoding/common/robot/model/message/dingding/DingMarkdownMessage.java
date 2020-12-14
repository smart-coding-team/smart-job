package cn.smartcoding.common.robot.model.message.dingding;

import cn.smartcoding.common.robot.model.message.Message;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DingMarkdownMessage implements Message {

    private String title;

    private List<String> items = new ArrayList<String>();

    private List<String> atMobileList = new ArrayList<>();

    private boolean isAtAll = false;

    public void addPhone(String phone) {
        atMobileList.add(phone);
    }

    public void appendText(String text) {
        items.add(text);
    }

    @Override
    public String toJsonString() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msgtype", "markdown");
        result.put("markdown", getMarkdown());
        result.put("at", getAt());
        return JSON.toJSONString(result);
    }

    private Map<String, Object> getAt() {
        Map<String, Object> at = new HashMap<>();
        at.put("atMobiles", atMobileList);
        at.put("isAtAll", isAtAll);
        return at;
    }

    private Map<String, Object> getMarkdown() {
        Map<String, Object> markdown = new HashMap<String, Object>();
        markdown.put("title", title);
        markdown.put("text", getText());
        return markdown;
    }

    private String getText() {
        StringBuilder markdownText = new StringBuilder();
        for (String item : items) {
            markdownText.append(item).append("\n");
        }
        return markdownText.toString();
    }
}
