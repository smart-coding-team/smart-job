package com.smartcoding.common.robot.exception;

import lombok.Getter;

/**
 * 通用异常
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 3:15 下午
 */
@Getter
public class WebHookRobotValidateException extends RuntimeException {
    protected int code = 100401;
    protected String message;

    public WebHookRobotValidateException(String message) {
        super(message);
        this.message = message;
    }

    public WebHookRobotValidateException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
