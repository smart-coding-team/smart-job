package cn.smartcoding.common.exception;


import cn.smartcoding.common.core.domain.CommonErrorCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 2017/03/10
 */
public class CommonException extends RuntimeException {

    protected String errMsg;
    protected String detailMsg;
    protected CommonErrorCode error;
    protected Object data;

    public CommonException(CommonErrorCode error) {
        super(error.getMessage());
        this.error = error;
        this.errMsg = error.getMessage();
    }

    public CommonException(CommonErrorCode error, String errMsg) {
        super(errMsg);
        this.error = error;
        this.errMsg = errMsg;
    }

    public CommonException(CommonErrorCode error, String errMsg, String detailMsg) {
        super(StringUtils.isEmpty(detailMsg) ? errMsg : detailMsg);
        this.error = error;
        this.errMsg = errMsg;
        this.detailMsg = detailMsg;
    }

    public CommonException(CommonErrorCode error, String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.error = error;
        this.errMsg = errMsg;
    }

    public CommonException(CommonErrorCode error, String errMsg, String detailMsg, Throwable cause) {
        super(StringUtils.isEmpty(detailMsg) ? errMsg : detailMsg, cause);
        this.error = error;
        this.errMsg = errMsg;
        this.detailMsg = detailMsg;
    }

    public CommonException(CommonErrorCode error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
        this.errMsg = error.getMessage();
    }

    public String getErrMsg() {
        return errMsg;
    }

    public CommonErrorCode getError() {
        return error;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public Object getData() {
        return data;
    }

    public CommonException setData(Object data) {
        this.data = data;
        return this;
    }
}
