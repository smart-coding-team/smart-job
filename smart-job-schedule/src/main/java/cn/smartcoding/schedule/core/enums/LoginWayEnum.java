

package cn.smartcoding.schedule.core.enums;

public enum LoginWayEnum {
    /**
     * 密码登录
     */
    password("password", "密码登录"),
    /**
     * sso登录
     */
    sso("sso", "sso登录");

    LoginWayEnum(String type, String des) {
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
