package cn.smartcoding.common.robot.model.message.wx;


import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import cn.smartcoding.common.robot.model.message.Message;
import cn.smartcoding.common.robot.util.ErrorStringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
public class WxTextMessage implements Message {


    private String text;

    private List<String> mentionedMobileList;

    private List<String> mentionedList;

    private boolean isAtAll;

    public WxTextMessage(String text) {
        this.text = text;
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "text");

        Map<String, Object> textContent = new HashMap<String, Object>();
        if (StrUtil.isBlank(text)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.wxError(" [text] should not be empty"));
        }
        textContent.put("content", text);
        if (isAtAll) {
            if (mentionedMobileList == null) {
                mentionedMobileList = new ArrayList<String>();
            }
            mentionedMobileList.add("@all");
        }
        if (mentionedMobileList != null && !mentionedMobileList.isEmpty()) {
            textContent.put("mentioned_mobile_list", mentionedMobileList);
        }
        if (mentionedList != null && !mentionedList.isEmpty()) {
            textContent.put("mentioned_list", mentionedList);
        }

        items.put("text", textContent);
        return JSON.toJSONString(items);
    }
}
