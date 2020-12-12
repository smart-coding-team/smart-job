package com.smartcoding.schedule.core.alarm.impl;

import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.schedule.core.alarm.AbstractJobAlarm;
import com.smartcoding.schedule.core.alarm.AlarmResult;
import com.smartcoding.schedule.core.alarm.JobAlarmEnum;
import com.smartcoding.schedule.core.exception.AlarmFailException;
import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(3)
public class SmsJobAlarm extends AbstractJobAlarm {

    @Override
    public AlarmResult sendAlarm(XxlAlarmInfo alarmInfo, List<SysUser> noticeUserList, String sendContent) throws AlarmFailException {
        throw new AlarmFailException("短信告警暂未实现");
    }

    @Override
    public boolean support(JobAlarmEnum jobAlarmEnum) {
        return JobAlarmEnum.SMS.equals(jobAlarmEnum);
    }
}
