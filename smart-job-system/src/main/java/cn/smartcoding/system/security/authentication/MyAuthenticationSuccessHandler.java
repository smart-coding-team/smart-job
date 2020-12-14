package cn.smartcoding.system.security.authentication;

import cn.smartcoding.common.constant.Constants;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.model.LoginUser;
import cn.smartcoding.common.utils.MessageUtils;
import cn.smartcoding.common.utils.ServletUtils;
import cn.smartcoding.framework.manager.AsyncManager;
import cn.smartcoding.framework.web.service.TokenService;
import cn.smartcoding.system.manager.factory.AsyncFactory;
import com.alibaba.fastjson.JSON;
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
