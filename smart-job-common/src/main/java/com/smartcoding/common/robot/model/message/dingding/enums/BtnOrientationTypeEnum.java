package com.smartcoding.common.robot.model.message.dingding.enums;

public enum BtnOrientationTypeEnum {
    /**
     * 0-按钮竖直排列
     */
    VERTICAL("0"),
    /**
     * 1-按钮横向排列
     */
    TRANSVERSE("1");

    private String code;

    BtnOrientationTypeEnum(String code) {
        this.code = code;
    }

    public static String formCode(String code) {
        if (code == null) {
            return null;
        }
        for (BtnOrientationTypeEnum value : BtnOrientationTypeEnum.values()) {
            if (value.code.endsWith(code)) {
                return code;
            }
        }
        return null;
    }
}
