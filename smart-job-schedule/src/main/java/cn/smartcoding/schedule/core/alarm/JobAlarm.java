package cn.smartcoding.schedule.core.alarm;

import cn.smartcoding.schedule.core.exception.AlarmFailException;

public interface JobAlarm {

    /**
     * send alarm
     *
     * @param alarmParam
     * @return
     */
    AlarmResult sendAlarm(AlarmParam alarmParam) throws AlarmFailException;

    /**
     * support type
     *
     * @param jobAlarmEnum
     * @return
     */
    boolean support(JobAlarmEnum jobAlarmEnum);
}
