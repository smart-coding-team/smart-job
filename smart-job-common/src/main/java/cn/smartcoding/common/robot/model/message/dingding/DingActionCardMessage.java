package cn.smartcoding.common.robot.model.message.dingding;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import cn.smartcoding.common.robot.model.message.Message;
import cn.smartcoding.common.robot.model.message.dingding.enums.BtnOrientationTypeEnum;
import cn.smartcoding.common.robot.model.message.dingding.enums.HideAvatarTypeEnums;
import cn.smartcoding.common.robot.util.ErrorStringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DingActionCardMessage implements Message {

    private String title;
    private String text;
    private String hideAvatar;
    private String btnOrientation;
    private List<Btn> btns;

    public static class Btn {
        /**
         * title : 内容不错
         * actionURL : https://www.dingtalk.com/
         */

        private String title;
        private String actionURL;

        public Btn() {
        }

        public Btn(String title, String actionURL) {
            this.title = title;
            this.actionURL = actionURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getActionURL() {
            return actionURL;
        }

        public void setActionURL(String actionURL) {
            this.actionURL = actionURL;
        }
    }

    @Override
    public String toJsonString() {

        if (StrUtil.isEmpty(title)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [title] should not be empty"));
        }
        if (StrUtil.isEmpty(text)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [text] should not be empty"));
        }
        if (CollectionUtil.isEmpty(btns)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [btns] should not be empty"));
        }
        Map<String, Object> items = new HashMap<String, Object>();

        Map<String, Object> content = new HashMap<String, Object>(5);
        content.put("title", title);
        content.put("text", text);
        content.put("btnOrientation", BtnOrientationTypeEnum.formCode(btnOrientation));
        content.put("hideAvatar", HideAvatarTypeEnums.formCode(hideAvatar));
        content.put("btns", btns);
        items.put("actionCard", content);
        items.put("msgtype", "actionCard");
        return JSON.toJSONString(items);
    }
}
