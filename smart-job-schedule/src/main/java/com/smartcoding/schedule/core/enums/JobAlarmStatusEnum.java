

package com.smartcoding.schedule.core.enums;

/**
 * @author 无缺
 * @date 2019-07-10
 */
public enum JobAlarmStatusEnum {
    // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
    /**
     * 默认
     */
    DEFAULT(0,"默认"),
    /**
     * 锁定状态
     */
    LOCK(-1,"锁定状态"),
    /**
     * 无需告警
     */
    NO_ALARM(1,"无需告警"),
    /**
     * 告警成功
     */
    OK(2,"告警成功"),
    /**
     * 告警失败
     */
    FAIL(3,"告警失败")
    ;

    JobAlarmStatusEnum(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    private Integer code;
    private String title;
    public String getTitle() {
        return title;
    }

    public Integer getCode() {
        return code;
    }
}
