package cn.smartcoding.schedule.core.alarm;

public interface AlarmManager {
    AlarmResult sendAlarm(AlarmParam alarmParam);

    AlarmResult testSendAlarm(AlarmParam alarmParam);
}
