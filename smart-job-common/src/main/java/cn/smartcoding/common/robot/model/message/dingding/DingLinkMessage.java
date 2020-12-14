package cn.smartcoding.common.robot.model.message.dingding;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import cn.smartcoding.common.robot.model.message.Message;
import cn.smartcoding.common.robot.util.ErrorStringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DingLinkMessage implements Message {

    private String title;
    private String text;
    private String picUrl;
    private String messageUrl;


    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "link");

        Map<String, String> linkContent = new HashMap<String, String>();
        if (StrUtil.isBlank(title)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError("DingLinkMessage title should not be blank"));
        }
        linkContent.put("title", title);

        if (StrUtil.isBlank(messageUrl)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError("DingLinkMessage messageUrl should not be blank"));
        }
        linkContent.put("messageUrl", messageUrl);

        if (StrUtil.isBlank(text)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError("DingLinkMessage text should not be blank"));
        }
        linkContent.put("text", text);

        if (StrUtil.isNotBlank(picUrl)) {
            linkContent.put("picUrl", picUrl);
        }

        items.put("link", linkContent);

        return JSON.toJSONString(items);
    }
}
