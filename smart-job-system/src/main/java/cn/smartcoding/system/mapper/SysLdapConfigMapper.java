package cn.smartcoding.system.mapper;

import cn.smartcoding.system.domain.SysLdapConfig;

/**
 * @author wuque
 */
public interface SysLdapConfigMapper {

    SysLdapConfig getSysDapConfig();

    int update(SysLdapConfig sysLdapConfig);

    int insert(SysLdapConfig sysLdapConfig);
}
