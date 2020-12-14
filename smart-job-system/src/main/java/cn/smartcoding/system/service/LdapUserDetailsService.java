package cn.smartcoding.system.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.core.domain.AccountCreateEnum;
import cn.smartcoding.common.core.domain.AccountStatusEnum;
import cn.smartcoding.common.core.domain.entity.SysUser;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.system.domain.SysLdapConfig;
import cn.smartcoding.system.dto.LdapUserDto;
import cn.smartcoding.system.security.authentication.AbstractPreAuthenticationChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

/**
 * Ldap用户验证处理
 *
 * @author wuque
 */
@Service
public class LdapUserDetailsService extends AbstractPreAuthenticationChecks implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(LdapUserDetailsService.class);

    @Resource
    private ISysUserService userService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private ISysLdapConfigService sysLdapConfigService;

    private SysLdapConfig sysLdapConfig;

    private LdapTemplate ldapTemplate;

    public void initLdapConfig() {
        sysLdapConfig = sysLdapConfigService.getSysDapConfig();
        ldapTemplate = sysLdapConfigService.getInstance(sysLdapConfig, true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        if (sysLdapConfig == null) {
            initLdapConfig();
        }
        LdapUserDto ldapUserDto = lookupByUsername(username);
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            //新增用户
            user = new SysUser();
            user.setUserName(username);
            user.setNickName(StrUtil.isNotEmpty(ldapUserDto.getDisplayName()) ? ldapUserDto.getDisplayName() : ldapUserDto.getSn());
            user.setEmail(ldapUserDto.getEmail());
            user.setMobile(ldapUserDto.getTelephone());
            user.setStatus(AccountStatusEnum.OK.getCode());
            user.setCreateType(AccountCreateEnum.LDAP.getCode());
            user.setCreateBy(AccountCreateEnum.LDAP.getTitle());
            user.setPassword(SecurityUtils.encryptPassword(UUID.randomUUID().toString()));
            userService.insertUserByLdap(user);
        }
        return getUserDetails(username, user, permissionService);
    }

    private LdapUserDto lookupByUsername(String username) {
        String userDnPatterns = sysLdapConfig.getUserDnPatterns();
        String userDn = StrFormatter.format(userDnPatterns, username);
        //查询用户信息
        return ldapTemplate.lookup(userDn, (AttributesMapper<LdapUserDto>) attributes -> {
            LdapUserDto person = new LdapUserDto();
            person.setDn(getAttributeValue(attributes, "cn"));
            person.setSn(getAttributeValue(attributes, "sn"));
            person.setEmail(getAttributeValue(attributes, sysLdapConfig.getAttributesMail()));
            person.setTelephone(getAttributeValue(attributes, sysLdapConfig.getAttributesTelephone()));

            return person;
        });
    }

    private String getAttributeValue(Attributes attributes, String attributeName) {
        Attribute cn = attributes.get(attributeName);
        try {
            return cn != null ? cn.get().toString() : "";
        } catch (NamingException e) {
            log.error("ldap获取属性:" + e.getMessage(), e);
            return "";
        }
    }

    public void authentication(String username, String password) throws BadCredentialsException {
        String userDnPatterns = sysLdapConfig.getUserDnPatterns();
        String userDn = StrFormatter.format(userDnPatterns, username);
        String principal = userDn + "," + sysLdapConfig.getBase();
        authenticationBydn(principal, password, ldapTemplate);

    }

    public void authenticationBydn(String principal, String password, LdapTemplate ldapTemplate) throws BadCredentialsException {
        DirContext ctx = null;
        //使用用户名、密码验证域用户
        try {
            ctx = ldapTemplate.getContextSource().getContext(principal, password);
        } catch (Exception e) {
            log.error("ldap验证账号和密码错误", e);
            throw new BadCredentialsException("账户名或者密码输入错误", e);

        } finally {
            if (ctx != null) {
                LdapUtils.closeContext(ctx);
            }
        }

    }
}
