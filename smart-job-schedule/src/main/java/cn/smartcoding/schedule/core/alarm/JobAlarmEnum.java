package cn.smartcoding.schedule.core.alarm;

public enum JobAlarmEnum {

    /**
     * 邮箱
     */
    EMAIL("email","邮箱"),
    /**
     * 钉钉
     */
    DING_DING("dingDing","钉钉"),
    /**
     * 企业微信
     */
    WX_WECHAT("wxWechat","企业微信"),
    /**
     * 飞书
     */
    feishu("feishu","飞书"),
    /**
     * 短信
     */
    SMS("sms","短信"),
    /**
     * 电话
     */
    MOBILE("mobile","电话"),;

    JobAlarmEnum(String code,String title) {
        this.code = code;
        this.title = title;
    }

    private String code;

    private String title;

    public String getCode() {
        return code;
    }


    public String getTitle() {
        return title;
    }


    public static JobAlarmEnum match(String name, JobAlarmEnum defaultItem) {
        if (name != null) {
            for (JobAlarmEnum item : JobAlarmEnum.values()) {
                if (item.code.equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
