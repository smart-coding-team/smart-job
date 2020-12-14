package cn.smartcoding.system.security.handle;

import cn.smartcoding.common.constant.Constants;
import cn.smartcoding.common.constant.HttpStatus;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.model.LoginUser;
import cn.smartcoding.common.utils.ServletUtils;
import cn.smartcoding.common.utils.StringUtils;
import cn.smartcoding.framework.manager.AsyncManager;
import cn.smartcoding.framework.web.service.TokenService;
import cn.smartcoding.system.manager.factory.AsyncFactory;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author wuque
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(ResultModel.success(HttpStatus.SUCCESS, "退出成功")));
    }
}
