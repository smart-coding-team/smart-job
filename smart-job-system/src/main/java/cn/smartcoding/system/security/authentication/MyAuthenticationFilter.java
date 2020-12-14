package cn.smartcoding.system.security.authentication;

import cn.smartcoding.system.security.LoginTypeEnum;
import cn.smartcoding.system.security.SecurityConstants;
import cn.smartcoding.system.service.SysLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信登录过滤器
 * Created on 2018/1/10.
 *
 * @author zlf
 * @since 1.0
 */
public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * request中必须含有mobile参数
     */
    private String usernameParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME;

    private String loginTypeParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_LOGIN_TYPE;
    /**
     * post请求
     */
    private boolean postOnly = true;

    private SysLoginService sysLoginService;

    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public MyAuthenticationFilter(SysLoginService sysLoginService) {
        /**
         * 处理的手机验证码登录请求处理url
         */
        super(new AntPathRequestMatcher("/api/login", "POST"));
    }

    public MyAuthenticationFilter() {
        /**
         * 处理的手机验证码登录请求处理url
         */
        super(new AntPathRequestMatcher("/api/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断是是不是post请求
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        AbstractAuthenticationToken authRequest = null;
        try {
            String username = obtainUsername(request);
            if (username != null) {
                request.setAttribute(usernameParameter, username);
            }
            //从请求中获取手机号码
            Integer loginType = obtainLoginType(request);
            String password = obtainPassword(request);
            LoginTypeEnum loginTypeEnum = LoginTypeEnum.fromCode(loginType);
            //图形验证码检查
            if (loginTypeEnum.isCaptchaValidate()) {
                String imageCode = obtainParam(request, SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE);
                String uuid = obtainParam(request, SecurityConstants.DEFAULT_PARAMETER_NAME_UUID);
                sysLoginService.captchaValidate(imageCode, uuid);
            }
            //TODO 去掉if else
            if (LoginTypeEnum.PASSWORD.equals(loginTypeEnum)) {
                authRequest = new UsernamePasswordAuthenticationToken(username, password);
            } else if (LoginTypeEnum.LDAP.equals(loginTypeEnum)) {
                authRequest = new LdapAuthenticationToken(username, password);
            } else {
                throw new AuthenticationServiceException("Authentication way not supported:" + loginTypeEnum);
            }
            //设置用户信息
            setDetails(request, authRequest);
            threadLocal.remove();

        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                throw e;
            }
            throw new AuthenticationServiceException("Authentication fail", e);
        }
        //返回Authentication实例
        Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
        return authenticate;
    }

    /**
     * 获取密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        return obtainParam(request, SecurityConstants.DEFAULT_PARAMETER_NAME_PASSWORD);
    }

    /**
     * 获取用户名
     */
    protected String obtainUsername(HttpServletRequest request) {
        return obtainParam(request, usernameParameter);
    }

    /**
     * 获取登录类型
     */
    protected Integer obtainLoginType(HttpServletRequest request) {
        String loginType = obtainParam(request, loginTypeParameter);
        return loginType != null ? Integer.valueOf(loginType) : null;
    }

    private String obtainParam(HttpServletRequest request, String parameter) {
        Map<String, Object> bodyParams = this.getBodyParams(request);
        String value = bodyParams.get(parameter) != null ? bodyParams.get(parameter).toString() : null;
        if (value == null) {
            value = request.getParameter(parameter);
        }
        return value;
    }


    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    /**
     * 获取body参数  body中的参数只能获取一次
     *
     * @param request
     * @return
     */
    private Map<String, Object> getBodyParams(HttpServletRequest request) {
        Map<String, Object> bodyParams = threadLocal.get();
        if (bodyParams == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream is = request.getInputStream()) {
                bodyParams = objectMapper.readValue(is, Map.class);
            } catch (IOException e) {

            }
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            threadLocal.set(bodyParams);
        }
        return bodyParams;
    }

    public void setSysLoginService(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    public SysLoginService getSysLoginService() {
        return sysLoginService;
    }
}
