

package cn.smartcoding.schedule.core.enums;

/**
 * 执行方式
 * @author 无缺
 * @date 2019-07-10
 */
public enum ExecutorWayTypeEnum {
    /**
     * 系统
     */
    SYSTEM(0,"系统执行"),
    /**
     * 手动执行
     */
    MANUAL(1,"手动执行"),
    ;

    ExecutorWayTypeEnum(Integer code, String title) {
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
