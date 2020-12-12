

package com.smartcoding.schedule.core.trigger;

import com.smartcoding.schedule.core.util.I18nUtil;

import java.util.Objects;

/**
 * 任务触发类型
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum TriggerTypeEnum {
    /**
     * 手动触发
     */
    MANUAL(0, I18nUtil.getString("jobconf_trigger_type_manual")),
    /**
     * Cron触发
     */
    CRON(1, I18nUtil.getString("jobconf_trigger_type_cron")),
    /**
     * 失败重试触发
     */
    RETRY(2, I18nUtil.getString("jobconf_trigger_type_retry")),
    /**
     * 父任务触发
     */
    PARENT(3, I18nUtil.getString("jobconf_trigger_type_parent")),
    /**
     * API触发
     */
    API(4, I18nUtil.getString("jobconf_trigger_type_api"));

    TriggerTypeEnum(int code, String title) {
        this.code = code;
        this.title = title;
    }

    private int code;

    private String title;

    public static TriggerTypeEnum matchCode(Integer code, TriggerTypeEnum defaultItem) {
        if (code == null) {
            return defaultItem;
        }
        for (TriggerTypeEnum item : TriggerTypeEnum.values()) {
            if (Objects.equals(code, item.getCode())) {
                return item;
            }
        }
        return defaultItem;
    }

    public String getTitle() {
        return title;
    }

    public int getCode() {
        return code;
    }

}
