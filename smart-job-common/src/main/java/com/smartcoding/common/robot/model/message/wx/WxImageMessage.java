package com.smartcoding.common.robot.model.message.wx;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.util.ErrorStringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@NoArgsConstructor
public class WxImageMessage implements Message {


    /**
     * 图片内容（base64编码前）的md5值
     */
    private String md5;
    /**
     * 图片内容的base64编码
     */
    private String base64;


    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "image");

        Map<String, Object> content = new HashMap<String, Object>();
        if (StrUtil.isBlank(md5)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.wxError("[md5] field should not be empty"));
        }
        if (StrUtil.isBlank(base64)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.wxError("[base64] field should not be empty"));
        }
        content.put("md5", md5);
        content.put("base64", base64);

        items.put("image", content);
        return JSON.toJSONString(items);
    }
}
