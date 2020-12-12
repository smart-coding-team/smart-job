package com.smartcoding.system.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常类
 *
 * @author wuque
 */
public class CaptchaException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public CaptchaException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>AuthenticationServiceException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}
