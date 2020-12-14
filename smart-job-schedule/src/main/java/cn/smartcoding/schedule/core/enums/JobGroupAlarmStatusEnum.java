

package cn.smartcoding.schedule.core.enums;

/**
 * @author 无缺
 * @date 2019-07-10
 */
public enum JobGroupAlarmStatusEnum {
    /**
     * 关闭
     */
    CLOSE(0,"关闭"),
    /**
     * 开启
     */
    OPEN(1,"开启"),
    ;

    JobGroupAlarmStatusEnum(Integer code, String title) {
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
