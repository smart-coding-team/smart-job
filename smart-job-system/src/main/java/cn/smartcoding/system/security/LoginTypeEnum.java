package cn.smartcoding.system.security;

import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.exception.CommonException;
import cn.smartcoding.system.security.authentication.LdapAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public enum LoginTypeEnum {

    /**
     * 用户名和密码登录
     */
    PASSWORD(1, "普通登录", true, UsernamePasswordAuthenticationToken.class),
    /**
     * LDAP登录
     */
    LDAP(2, "LDAP登录", true, LdapAuthenticationToken.class);

    private final Integer code;

    private final String desc;

    private final boolean captchaValidate;

    private Class <?extends AbstractAuthenticationToken> clazz;

    LoginTypeEnum(Integer code, String desc, boolean captchaValidate, Class <?extends AbstractAuthenticationToken> clazz) {
        this.code = code;
        this.desc = desc;
        this.captchaValidate = captchaValidate;
        this.clazz = clazz;
    }

    public static LoginTypeEnum fromCode(Integer code) {

        LoginTypeEnum[] values = LoginTypeEnum.values();
        for (LoginTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new CommonException(CommonErrorCode.UNSUPPORTED_TYPE, "不支持的登录类型");
    }

    public Class<? extends AbstractAuthenticationToken> getClazz() {
        return clazz;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isCaptchaValidate() {
        return captchaValidate;
    }
}
