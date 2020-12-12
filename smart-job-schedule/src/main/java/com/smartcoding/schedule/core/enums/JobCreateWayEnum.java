

package com.smartcoding.schedule.core.enums;

/**
 * 任务创建方式
 */
public enum JobCreateWayEnum {
    /**
     * 界面创建
     */
    MANUAL_CREATE(0, "界面创建"),
    /**
     * API创建
     */
    AUTO_CREATE(1, "API创建");

    JobCreateWayEnum(Integer code, String title) {
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


    public static JobCreateWayEnum fromCode(Integer code, JobCreateWayEnum defaultEnum) {
        JobCreateWayEnum[] values = JobCreateWayEnum.values();
        for (JobCreateWayEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultEnum;
    }
}
