

package cn.smartcoding.schedule.core.enums;

/**
 * 注册方式
 * @author 无缺
 * @date 2019-07-10
 */
public enum AddressTypeEnum {
    /**
     * 自动注册
     */
    AUTO(0,"自动注册"),
    /**
     * 手动录入
     */
    ADD(1,"手动录入"),
    ;

    AddressTypeEnum(Integer code, String title) {
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
    public static  AddressTypeEnum fromCode(Integer code,AddressTypeEnum defaultEnum) {

        AddressTypeEnum[] values = AddressTypeEnum.values();
        for (AddressTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return defaultEnum;
    }
}
