package cn.smartcoding.common.robot.model.message.dingding;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import cn.smartcoding.common.robot.model.message.Message;
import cn.smartcoding.common.robot.model.message.dingding.enums.BtnOrientationTypeEnum;
import cn.smartcoding.common.robot.model.message.dingding.enums.HideAvatarTypeEnums;
import cn.smartcoding.common.robot.util.ErrorStringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DingSingleActionCardMessage implements Message {


    private String title;
    private String text;
    private String singleTitle;
    private String singleURL;
    private String btnOrientation;
    private String hideAvatar;


    @Override
    public String toJsonString() {

        if (StrUtil.isEmpty(text)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [text] should not be empty"));
        }
        if (StrUtil.isEmpty(title)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [title] should not be empty"));
        }
        if (StrUtil.isEmpty(singleTitle)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [singleTitle] should not be empty"));
        }
        if (StrUtil.isEmpty(singleURL)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [singleURL] should not be empty"));
        }
        Map<String, Object> items = new HashMap<String, Object>();

        Map<String, String> content = new HashMap<String, String>(6);
        content.put("title", title);
        content.put("text", text);
        content.put("singleTitle", singleTitle);
        content.put("singleURL", singleURL);
        content.put("btnOrientation", BtnOrientationTypeEnum.formCode(btnOrientation));
        content.put("hideAvatar", HideAvatarTypeEnums.formCode(hideAvatar));

        items.put("msgtype", "actionCard");
        items.put("actionCard", content);
        return JSON.toJSONString(items);
    }
}
