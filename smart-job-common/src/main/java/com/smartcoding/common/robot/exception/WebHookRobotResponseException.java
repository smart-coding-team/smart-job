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
public class WebHookRobotResponseException extends RuntimeException {
    protected int code = 100402;
    protected String message;

    public WebHookRobotResponseException(String message) {
        super(message);
        this.message = message;
    }

    public WebHookRobotResponseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
