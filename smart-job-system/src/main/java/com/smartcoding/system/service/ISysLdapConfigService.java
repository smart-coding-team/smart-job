package com.smartcoding.system.service;

import com.smartcoding.system.domain.SysLdapConfig;
import org.springframework.ldap.core.LdapTemplate;

public interface ISysLdapConfigService {

    SysLdapConfig getSysDapConfig();

    int update(SysLdapConfig sysLdapConfig);

    int addLdapConfig(SysLdapConfig config);

    void testLdap(SysLdapConfig config);

    LdapTemplate getInstance(SysLdapConfig sysLdapConfig);

    LdapTemplate getInstance(SysLdapConfig sysLdapConfig, boolean cache);
}
