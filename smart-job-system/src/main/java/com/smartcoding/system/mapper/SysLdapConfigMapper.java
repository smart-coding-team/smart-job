package com.smartcoding.system.mapper;

import com.smartcoding.system.domain.SysLdapConfig;

/**
 * @author wuque
 */
public interface SysLdapConfigMapper {

    SysLdapConfig getSysDapConfig();

    int update(SysLdapConfig sysLdapConfig);

    int insert(SysLdapConfig sysLdapConfig);
}
