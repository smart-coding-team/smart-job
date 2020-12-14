package cn.smartcoding.common.core.domain;


import cn.smartcoding.common.exception.CommonException;
import cn.smartcoding.common.utils.uuid.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * ResultModel 返回结果类
 *
 * @author
 */
public class ResultModel<T> implements Serializable {

    private String requestId;
    private int code;
    private String msg;
    private T data;


    private String errorStackTrace;

    public ResultModel() {

    }

    public static ResultModel success() {

        return ResultModel.success(null);
    }

    public ResultModel(int code, String msg, T data, String errorStackTrace) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.errorStackTrace = errorStackTrace;
        this.requestId = UUID.fastUUID().toString();
    }

    public static <T> ResultModel<T> success(T data) {
        return success(data, CommonErrorCode.SUCCESS.getMessage());
    }

    public static <T> ResultModel<T> success(T data, String msg) {
        return new ResultModel<>(CommonErrorCode.SUCCESS.getCode(), msg, data, null);
    }

    public static <T> ResultModel<T> success(String msg) {
        return new ResultModel<>(CommonErrorCode.SUCCESS.getCode(), msg, null, null);
    }

    public static <T> ResultModel<T> fail() {

        return new ResultModel<>(CommonErrorCode.ERROR.getCode(), CommonErrorCode.ERROR.getMessage(), null, null);
    }

    public static <T> ResultModel<T> fail(CommonException ex) {
        String resultMsg = ex.getError().getMessage();
        if (StringUtils.isNotEmpty(ex.getErrMsg())) {
            resultMsg = ex.getErrMsg();
        }
        return new ResultModel<>(ex.getError().getCode(), resultMsg, null, ex.getMessage());
    }

    public static <T> ResultModel<T> fail(CommonException ex, T data) {
        String resultMsg = ex.getError().getMessage();
        if (StringUtils.isNotEmpty(ex.getErrMsg())) {
            resultMsg = ex.getErrMsg();
        }
        return new ResultModel<>(ex.getError().getCode(), resultMsg, data, ex.getMessage());
    }

    public static <T> ResultModel<T> fail(CommonErrorCode error) {
        return new ResultModel<>(error.getCode(), error.getMessage(), null, null);
    }

    public static ResultModel error(String s) {
        return new ResultModel<>(CommonErrorCode.ERROR.getCode(), s, null, null);
    }

    public static ResultModel error(int code, String s) {
        return new ResultModel<>(code, s, null, null);
    }

    public static <T> ResultModel<T> fail(CommonErrorCode error, String msg) {
        return new ResultModel<>(error.getCode(), msg, null, null);
    }

    public static <T> ResultModel<T> fail(int error, T data, String msg) {
        return new ResultModel<>(error, msg, data, null);
    }

    public static <T> ResultModel<T> fail(CommonErrorCode error, Throwable ex) {
        return new ResultModel<>(error.getCode(), error.getMessage(), null, ex.getMessage());
    }

    public static <T> ResultModel<T> fail(CommonErrorCode error, String msg, Throwable ex) {
        return new ResultModel<>(error.getCode(), msg, null, ex.getMessage());
    }

    public int getCode() {
        return code;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return code == CommonErrorCode.SUCCESS.getCode();
    }

    @JsonIgnore
    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
