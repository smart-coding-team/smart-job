

package cn.smartcoding.schedule.core.trigger;

/**
 * 任务触发状态
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum TriggerStatusEnum {
    /**
     * 已停止
     */
    STOPPING(0,"已停止"),
    /**
     * 已就绪
     */
    READY(1,"已就绪");

    TriggerStatusEnum(Integer code, String title) {
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

    public static TriggerStatusEnum fromCode(Integer code, TriggerStatusEnum defaultEnum) {
        TriggerStatusEnum[] values = TriggerStatusEnum.values();
        for (TriggerStatusEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultEnum;
    }
}
