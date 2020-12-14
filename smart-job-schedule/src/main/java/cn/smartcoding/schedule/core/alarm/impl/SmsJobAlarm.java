package cn.smartcoding.schedule.core.alarm.impl;

import cn.smartcoding.common.core.domain.entity.SysUser;
import cn.smartcoding.schedule.core.alarm.AbstractJobAlarm;
import cn.smartcoding.schedule.core.alarm.AlarmResult;
import cn.smartcoding.schedule.core.alarm.JobAlarmEnum;
import cn.smartcoding.schedule.core.exception.AlarmFailException;
import cn.smartcoding.schedule.core.model.XxlAlarmInfo;
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
