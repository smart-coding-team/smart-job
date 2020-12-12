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
public class WebHookRobotIllegalArgumentException extends RuntimeException {
    protected int code = 100403;
    protected String message;

    public WebHookRobotIllegalArgumentException(String message) {
        super(message);
        this.message = message;
    }

    public WebHookRobotIllegalArgumentException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
