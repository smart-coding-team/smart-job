

package com.smartcoding.schedule.core.enums;

public enum JobGroupClusterEnum {
    /**
     * 开发环境
     */
    DEV("dev", "开发环境"),
    /**
     * 测试环境
     */
    TEST("test", "测试环境"),
    /**
     * 预发环境
     */
    PRE("pre", "预发环境"),
    /**
     * 正式环境
     */
    PROD("prod", "正式环境");

    JobGroupClusterEnum(String type, String des) {
        this.type = type;
        this.des = des;
    }

    private String type;
    private String des;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
