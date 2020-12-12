package com.smartcoding.system.security.authentication;

import com.alibaba.fastjson.JSON;
import com.smartcoding.common.constant.Constants;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.core.domain.model.LoginUser;
import com.smartcoding.common.utils.MessageUtils;
import com.smartcoding.common.utils.ServletUtils;
import com.smartcoding.framework.manager.AsyncManager;
import com.smartcoding.framework.web.service.TokenService;
import com.smartcoding.system.manager.factory.AsyncFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        String usename = loginUser.getUsername();
        String token = tokenService.createToken(loginUser);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(Constants.TOKEN, token);
        ServletUtils.renderString(response, JSON.toJSONString(ResultModel.success(tokenMap)));
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(usename, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
    }
}
