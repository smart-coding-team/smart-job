package cn.smartcoding.common.robot.model.message.wx;

import cn.smartcoding.common.robot.model.message.Message;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WxMarkdownMessage implements Message {

    private List<String> items = new ArrayList<String>();

    public void add(String text) {
        items.add(text);
    }


    @Override
    public String toJsonString() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msgtype", "markdown");

        Map<String, Object> markdown = new HashMap<String, Object>();

        StringBuilder markdownText = new StringBuilder();
        for (String item : items) {
            markdownText.append(item).append("\n");
        }
        markdown.put("content", markdownText.toString());
        result.put("markdown", markdown);

        return JSON.toJSONString(result);
    }
}
