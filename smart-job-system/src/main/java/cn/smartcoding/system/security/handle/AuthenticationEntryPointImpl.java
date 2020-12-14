package cn.smartcoding.system.security.handle;

import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.utils.ServletUtils;
import cn.smartcoding.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author wuque
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        String message = e.getMessage();
        String msg = message != null ? message : StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(ResultModel.fail(CommonErrorCode.UNAUTHORIZED, msg)));
    }
}
