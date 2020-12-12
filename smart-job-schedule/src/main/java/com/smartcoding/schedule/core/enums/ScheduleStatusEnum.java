

package com.smartcoding.schedule.core.enums;

/**
 * 运行状态
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum ScheduleStatusEnum {
    /**
     * 关闭
     */
    CLOSE(0, "关闭"),
    /**
     * 开启
     */
    OPEN(1, "开启");


    ScheduleStatusEnum(Integer code, String title) {
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

    public static ScheduleStatusEnum fromCode(Integer code, ScheduleStatusEnum defaultEnum) {
        ScheduleStatusEnum[] values = ScheduleStatusEnum.values();
        for (ScheduleStatusEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultEnum;
    }

}
