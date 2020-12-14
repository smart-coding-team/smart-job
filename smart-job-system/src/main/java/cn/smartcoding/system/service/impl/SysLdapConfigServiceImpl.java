package cn.smartcoding.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.exception.CommonException;
import cn.smartcoding.common.utils.AESUtil;
import cn.smartcoding.system.domain.SysLdapConfig;
import cn.smartcoding.system.exception.LdapAuthenticationException;
import cn.smartcoding.system.exception.LdapLockAuthenticationException;
import cn.smartcoding.system.mapper.SysLdapConfigMapper;
import cn.smartcoding.system.service.ISysLdapConfigService;
import cn.smartcoding.system.service.LdapUserDetailsService;
import org.springframework.ldap.core.AuthenticationSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SysLdapConfigServiceImpl implements ISysLdapConfigService {

    private static final String key = "AD42F6697B035B75";
    @Resource
    private SysLdapConfigMapper sysLdapConfigMapper;

    @Resource
    private LdapUserDetailsService ldapUserDetailsService;

    private Map<String, Long> cacheLdapTemplate = new HashMap<>();

    @Override
    public SysLdapConfig getSysDapConfig() {
        SysLdapConfig sysDapConfig = sysLdapConfigMapper.getSysDapConfig();
        if (sysDapConfig != null && StrUtil.isNotEmpty(sysDapConfig.getManagerPassword())) {
            sysDapConfig.setManagerPassword(AESUtil.decrypt(sysDapConfig.getManagerPassword(), key));
        }
        return sysDapConfig;
    }

    @Override
    public int update(SysLdapConfig sysLdapConfig) {

        String managerPassword = sysLdapConfig.getManagerPassword();
        if (StrUtil.isNotEmpty(managerPassword)) {
            sysLdapConfig.setManagerPassword(AESUtil.encrypt(managerPassword, key));
        }
        return sysLdapConfigMapper.update(sysLdapConfig);
    }

    @Override
    public int addLdapConfig(SysLdapConfig sysLdapConfig) {
        SysLdapConfig sysDapConfig = sysLdapConfigMapper.getSysDapConfig();
        if (sysDapConfig != null) {
            throw new CommonException(CommonErrorCode.ERROR, "已经存在LDAP配置");
        }
        String managerPassword = sysLdapConfig.getManagerPassword();
        if (StrUtil.isNotEmpty(managerPassword)) {
            sysLdapConfig.setManagerPassword(AESUtil.encrypt(managerPassword, key));
        }
        return sysLdapConfigMapper.insert(sysLdapConfig);
    }

    @Override
    public void testLdap(SysLdapConfig sysLdapConfig) {
        checkLdapConfig(sysLdapConfig);
        LdapTemplate ldapTemplate = getInstance(sysLdapConfig);
        ldapUserDetailsService.authenticationBydn(sysLdapConfig.getManagerDn(), sysLdapConfig.getManagerPassword(), ldapTemplate);
    }

    @Override
    public LdapTemplate getInstance(SysLdapConfig sysLdapConfig) {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setCacheEnvironmentProperties(false);
        ldapContextSource.setUrl(sysLdapConfig.getUrls());
        ldapContextSource.setBase(sysLdapConfig.getBase());
        ldapContextSource.setUserDn(sysLdapConfig.getManagerDn());
        ldapContextSource.setPassword(sysLdapConfig.getManagerPassword());
        ldapContextSource.setAuthenticationSource(new AuthenticationSource() {
            @Override
            public String getCredentials() {
                return sysLdapConfig.getManagerPassword();
            }

            @Override
            public String getPrincipal() {
                return sysLdapConfig.getManagerDn();
            }
        });
        LdapTemplate ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(ldapContextSource);
        return ldapTemplate;
    }

    @Override
    public LdapTemplate getInstance(SysLdapConfig sysLdapConfig, boolean cache) {
        checkLdapConfig(sysLdapConfig);
        if (!cache) {
            return getInstance(sysLdapConfig);
        }
        long time = sysLdapConfig.getGmtModified().getTime();
        if (Objects.equals(cacheLdapTemplate.get("ldapTemplate"), time)) {
            return getInstance(sysLdapConfig);
        } else {
            LdapTemplate instance = getInstance(sysLdapConfig);
            cacheLdapTemplate.put("ldapTemplate", time);
            return instance;
        }
    }

    private void checkLdapConfig(SysLdapConfig sysLdapConfig) {
        if (sysLdapConfig == null) {
            throw new LdapAuthenticationException("管理员未设置LDAP的参数,请联系管理员");
        }

        if (!Boolean.TRUE.equals(sysLdapConfig.getEnabled())) {
            throw new LdapLockAuthenticationException("管理员已禁止LDAP登录,请联系管理员");
        }
    }
}
