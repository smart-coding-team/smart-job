package cn.smartcoding.system.security.authentication;

import cn.smartcoding.system.service.LdapUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信登录验证逻辑
 * Created on 2018/1/10.
 *
 * @author zlf
 * @since 1.0
 */
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private LdapUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LdapAuthenticationToken authenticationToken = (LdapAuthenticationToken) authentication;

        userDetailsService.initLdapConfig();
        //验证账号是否成功
        userDetailsService.authentication((String) authenticationToken.getPrincipal(), (String) authenticationToken.getCredentials());

        //调用自定义的userDetailsService认证 手机号码
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if (user == null || StringUtils.isEmpty(user.getUsername())) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        //如果user不为空重新构建MobileCodeAuthenticationToken（已认证）
        LdapAuthenticationToken authenticationResult = new LdapAuthenticationToken(user, user.getAuthorities());

        authenticationResult.setDetails(user);

        return authenticationResult;
    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return LdapAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public LdapUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(LdapUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
