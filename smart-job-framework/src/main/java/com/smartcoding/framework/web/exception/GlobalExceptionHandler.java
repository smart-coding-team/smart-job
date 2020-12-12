package com.smartcoding.framework.web.exception;

import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.exception.BaseException;
import com.smartcoding.common.exception.CommonException;
import com.smartcoding.common.exception.CustomException;
import com.smartcoding.common.exception.DemoModeException;
import com.smartcoding.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 *
 * @author wuque
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public ResultModel baseException(BaseException e) {
        return ResultModel.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public ResultModel businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return ResultModel.error(e.getMessage());
        }
        log.error(e.getMessage(), e);
        return ResultModel.error(e.getCode(), e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CommonException.class)
    public ResultModel CommonException(CommonException e) {
        CommonErrorCode error = e.getError();
        if (error == null) {
            return ResultModel.error(e.getMessage());
        }
        log.error(e.getMessage(), e);
        return ResultModel.error(error.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultModel handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultModel.fail(CommonErrorCode.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResultModel handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResultModel.fail(CommonErrorCode.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResultModel handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return ResultModel.error(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultModel handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResultModel.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultModel handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultModel.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ResultModel validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResultModel.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultModel.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public ResultModel demoModeException(DemoModeException e) {
        return ResultModel.error("演示模式，不允许操作");
    }
}
