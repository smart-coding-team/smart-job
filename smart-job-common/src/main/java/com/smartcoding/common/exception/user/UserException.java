package com.smartcoding.common.exception.user;

import com.smartcoding.common.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author wuque
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

    public UserException(String code, Object[] args, String message) {
        super("user", code, args, message);
    }
}
