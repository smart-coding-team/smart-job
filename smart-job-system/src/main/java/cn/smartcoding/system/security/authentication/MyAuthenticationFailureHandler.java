package cn.smartcoding.system.security.authentication;

import cn.smartcoding.common.constant.Constants;
import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.utils.ServletUtils;
import cn.smartcoding.framework.manager.AsyncManager;
import cn.smartcoding.system.manager.factory.AsyncFactory;
import cn.smartcoding.system.security.SecurityConstants;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME);
        String message = exception.getMessage();
        ServletUtils.renderString(response, JSON.toJSONString(ResultModel.fail(CommonErrorCode.ERROR, message)));
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, message));
    }
}
