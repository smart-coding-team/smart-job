package cn.smartcoding.schedule.core.alarm;


import java.util.Date;

/**
 * @author wuque
 */
public class DefaultFailAlarmResult implements AlarmResult {

    private String errorMsg;

    public DefaultFailAlarmResult(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean getSendStatus() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public Date getSendDate() {
        return new Date();
    }
}
