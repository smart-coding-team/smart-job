package com.smartcoding.common.robot.model.message.dingding;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.util.ErrorStringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DingTextMessage implements Message {

    private String text = "text";
    private List<String> atMobiles;
    private boolean isAtAll;

    public DingTextMessage() {
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "text");

        Map<String, String> textContent = new HashMap<String, String>();
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException(ErrorStringUtils.dingdingError("DingTextMessage text字段 should not be blank"));
        }
        textContent.put("content", text);
        items.put("text", textContent);

        Map<String, Object> atItems = new HashMap<String, Object>();
        if (atMobiles != null && !atMobiles.isEmpty()) {
            atItems.put("atMobiles", atMobiles);
        }
        if (isAtAll) {
            atItems.put("isAtAll", isAtAll);
        }
        items.put("at", atItems);

        return JSON.toJSONString(items);
    }
}
