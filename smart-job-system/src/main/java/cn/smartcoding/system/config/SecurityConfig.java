package cn.smartcoding.system.config;

import cn.smartcoding.framework.web.service.TokenService;
import cn.smartcoding.system.security.authentication.*;
import cn.smartcoding.system.security.filter.JwtAuthenticationTokenFilter;
import cn.smartcoding.system.security.handle.AuthenticationEntryPointImpl;
import cn.smartcoding.system.security.handle.LogoutSuccessHandlerImpl;
import cn.smartcoding.system.service.LdapUserDetailsService;
import cn.smartcoding.system.service.SysLoginService;
import cn.smartcoding.system.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * spring security配置
 *
 * @author wuque
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义用户认证逻辑
     */
    @Resource
    private UserDetailsServiceImpl userDetailsService;


    /**
     * 自定义用户认证逻辑
     */
    @Resource
    private LdapUserDetailsService ldapUserDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    ///**
    // * token认证过滤器
    // */
    //@Autowired
    //private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    @Resource
    private TokenService tokenService;

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private SysLoginService sysLoginService;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;


    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CRSF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 验证码captchaImage 允许匿名访问
                .antMatchers("/login", "/api/captchaImage", "/api/code").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/api").anonymous()
                .antMatchers("/api/profile/**").anonymous()
                .antMatchers("/api/common/download**").anonymous()
                .antMatchers("/api/common/download/resource**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/api/druid/**").anonymous()
                .antMatchers("/actuator/**").anonymous()
                .antMatchers("/checkpreload.htm").anonymous()
                .antMatchers("/makeFail.htm").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();

        httpSecurity.logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccessHandler);

        // // 添加JWT filter
        httpSecurity.addFilterBefore(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.addFilter(new JwtAuthenticationTokenFilter(authenticationManager, tokenService));
        ////
        // httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        // // 添加CORS filter
        // httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }


    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ldapAuthenticationProvider()).authenticationProvider(usernamePasswordAuthenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public MyAuthenticationFilter myAuthenticationFilter() {
        MyAuthenticationFilter filter = new MyAuthenticationFilter();
        filter.setSysLoginService(sysLoginService);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider();
        ldapAuthenticationProvider.setUserDetailsService(ldapUserDetailsService);
        return ldapAuthenticationProvider;
    }

    @Bean
    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
        UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider = new UsernamePasswordAuthenticationProvider();
        usernamePasswordAuthenticationProvider.setUserDetailsService(userDetailsService);
        usernamePasswordAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return usernamePasswordAuthenticationProvider;
    }
}
