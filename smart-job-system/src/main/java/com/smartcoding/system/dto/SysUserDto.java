package com.smartcoding.system.dto;

import com.smartcoding.common.core.domain.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class SysUserDto implements Serializable {
    private SysUser user;
    private Set<String> roles;
    private Set<String> permissions;
}
