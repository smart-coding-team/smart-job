package com.smartcoding.common.core.domain;

/**
 * 帐号状态
 */
public enum AccountStatusEnum {

    /**
     * 正常
     */
    OK(0, "正常"),
    /**
     * 停用
     */
    STOP(1, "停用"),
    /**
     * 锁定
     */
    LOCK(2, "锁定");


    AccountStatusEnum(int code, String title) {
        this.code = code;
        this.title = title;
    }

    private int code;
    private String title;

    public String getTitle() {
        return title;
    }

    public int getCode() {
        return code;
    }

    public static AccountStatusEnum fromCode(Integer code, AccountStatusEnum defaultEnum) {
        if (code == null) {
            return defaultEnum;
        }
        AccountStatusEnum[] values = AccountStatusEnum.values();
        for (AccountStatusEnum value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return defaultEnum;
    }

}
