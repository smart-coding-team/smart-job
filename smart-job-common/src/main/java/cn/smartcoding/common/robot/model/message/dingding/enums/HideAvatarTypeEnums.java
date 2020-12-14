package cn.smartcoding.common.robot.model.message.dingding.enums;

public enum HideAvatarTypeEnums {
    /**
     * 0-正常发消息者头像
     */
    SHOW("0"),
    /**
     * 1-隐藏发消息者头像
     */
    HIDE("1");

    private String code;

    HideAvatarTypeEnums(String code) {
        this.code = code;
    }

    public static String formCode(String code) {
        if (code == null) {
            return null;
        }
        for (HideAvatarTypeEnums value : HideAvatarTypeEnums.values()) {
            if (value.code.endsWith(code)) {
                return code;
            }
        }
        return null;
    }
}
