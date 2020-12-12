package com.smartcoding.schedule.core.alarm;

import java.util.Date;

public class DefaultAlarmResult implements AlarmResult {
    private Boolean sendStatus;
    private String errorMsg;
    private Date sendDate;

    public DefaultAlarmResult(Boolean sendStatus, String errorMsg) {
        this.sendStatus = sendStatus;
        this.errorMsg = errorMsg;
        this.sendDate = new Date();
    }

    public DefaultAlarmResult(Boolean sendStatus, String errorMsg, Date sendDate) {
        this.sendStatus = sendStatus;
        this.errorMsg = errorMsg;
        this.sendDate = sendDate;
    }

    @Override
    public boolean getSendStatus() {
        return Boolean.TRUE.equals(sendStatus);
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public Date getSendDate() {
        return sendDate;
    }
}
