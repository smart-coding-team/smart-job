package com.smartcoding.system.service;

import com.smartcoding.common.constant.Constants;
import com.smartcoding.common.core.domain.model.LoginUser;
import com.smartcoding.common.core.redis.RedisCache;
import com.smartcoding.common.utils.MessageUtils;
import com.smartcoding.framework.web.service.TokenService;
import com.smartcoding.system.security.CaptchaException;
import com.smartcoding.system.security.CaptchaExpireException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 *
 * @author wuque
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        //captchaValidate(username, code, uuid);
        //// 用户验证
        //Authentication authentication = null;
        //try {
        //    // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        //    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //} catch (Exception e) {
        //    if (e instanceof BadCredentialsException) {
        //        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
        //        throw new UserPasswordNotMatchException();
        //    } else {
        //        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
        //        throw new CustomException(e.getMessage());
        //    }
        //}
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 登录验证
     *
     * @param imageCode 验证码
     * @param uuid      唯一标识
     * @return 结果
     */
    public void captchaValidate(String imageCode, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException(MessageUtils.message("user.jcaptcha.expire"));
        }
        if (!imageCode.equalsIgnoreCase(captcha)) {
            throw new CaptchaException(MessageUtils.message("user.jcaptcha.error"));
        }
    }

}
