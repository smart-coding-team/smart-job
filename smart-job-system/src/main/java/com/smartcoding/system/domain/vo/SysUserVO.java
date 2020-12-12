package com.smartcoding.system.domain.vo;

import lombok.Data;

@Data
public class SysUserVO {

    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 帐号状态
     */
    private String status;
}
