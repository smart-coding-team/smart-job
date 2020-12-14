

package cn.smartcoding.schedule.core.enums;

/**
 * @author 无缺
 * @date 2019-07-10
 */
public enum AddressOnlineEnum {
    /**
     * 离线
     */
    OFFLINE(0,"离线"),
    /**
     * 在线
     */
    ONLINE(1,"在线"),
    ;

    AddressOnlineEnum(Integer code, String title) {
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
