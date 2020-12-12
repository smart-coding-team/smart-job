package com.smartcoding.schedule.core.alarm;

import java.util.Date;

public interface AlarmResult {

    boolean getSendStatus();

    String  getErrorMsg();

    Date getSendDate();

}
