package com.smartcoding.common.core.domain;

/**
 * 帐号创建类型
 */
public enum AccountCreateEnum {
    /**
     * 系统
     */
    SYS(0, "系统"),
    /**
     * ldap
     */
    LDAP(1, "ldap");


    AccountCreateEnum(int code, String title) {
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

    public static AccountCreateEnum fromCode(Integer code, AccountCreateEnum defaultEnum) {
        if (code == null) {
            return defaultEnum;
        }
        AccountCreateEnum[] values = AccountCreateEnum.values();
        for (AccountCreateEnum value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return defaultEnum;
    }

}
