package com.smartcoding.system.security.authentication;

import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.common.core.domain.model.LoginUser;
import com.smartcoding.common.utils.StringUtils;
import com.smartcoding.system.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public abstract class AbstractPreAuthenticationChecks {

    protected void preAuthenticationChecks(String username, Object user) {
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
    }

    protected void preAuthenticationChecks(UserDetails user) {
        String username = user.getUsername();
        if (!user.isAccountNonLocked()) {
            log.info("登录用户：{} 已被锁定.", username);
            throw new LockedException("对不起，您的账号：" + username + " 已锁定");
        }

        if (!user.isEnabled()) {
            log.info("登录用户：{} 已被停用.", username);
            throw new DisabledException("对不起，您的账号：" + username + " 已停用");
        }

        if (!user.isAccountNonExpired()) {
            log.info("登录用户：{} 已过期.", username);
            throw new AccountExpiredException("对不起，您的账号：" + username + " 已过期");
        }
        if (!user.isCredentialsNonExpired()) {
            log.info("登录用户：{} 密码过期.", username);
            throw new CredentialsExpiredException("对不起，您的账号：" + username + " 密码过期");
        }
    }

    protected UserDetails getUserDetails(String username, SysUser user, SysPermissionService permissionService) {
        preAuthenticationChecks(username, user);
        LoginUser loginUser = new LoginUser(user);
        preAuthenticationChecks(loginUser);
        loginUser.setPermissions(permissionService.getMenuPermission(user.isAdmin(), user.getUserId()));
        return loginUser;
    }
}
