package com.smartcoding.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Ldap用户
 */
@Data
public class LdapUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    private String dn;

    /**
     * 用户昵称
     */
    private String sn;

    /**
     * 用户显示名
     */
    private String displayName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String telephone;

    private String password;
}
