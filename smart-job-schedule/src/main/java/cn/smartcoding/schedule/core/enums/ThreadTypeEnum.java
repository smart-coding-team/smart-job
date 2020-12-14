

package cn.smartcoding.schedule.core.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度线程的类型
 *
 * @author 无缺
 * @date 2019-07-10
 */
public enum ThreadTypeEnum {
    /**
     * 客户端注册心跳线程
     */
    JOB_REGISTRY_MONITOR_HELPER(0, "客户端注册心跳线程"),
    /**
     * 任务调度线程
     */
    JOB_SCHEDULE_HELPER(1, "任务调度线程"),
    /**
     * 任务失败检查线程
     */
    JOB_FAIL_MONITOR_HELPER(2, "任务失败检查线程"),
    ;

    ThreadTypeEnum(Integer code, String title) {
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

    public static ThreadTypeEnum fromCode(Integer code, ThreadTypeEnum defaultEnum) {
        ThreadTypeEnum[] values = ThreadTypeEnum.values();
        for (ThreadTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultEnum;
    }
    public static List<Integer> getCodeList(){
        ThreadTypeEnum[] values = ThreadTypeEnum.values();
        List<Integer> list = new ArrayList<>(values.length);
        for (ThreadTypeEnum value : values) {
            list.add(value.getCode());
        }
        return list;
    }
}
