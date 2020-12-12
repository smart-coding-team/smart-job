package com.smartcoding.schedule.core.alarm.impl;

import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.schedule.core.alarm.AbstractJobAlarm;
import com.smartcoding.schedule.core.alarm.AlarmParam;
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
@Order(5)
public class EmailJobAlarm extends AbstractJobAlarm {

    //// email alarm template
    //private static final String MAIL_BODY_TEMPLATE = "<h5>" + I18nUtil.getString("jobconf_monitor_detail") + "：</span>" +
    //        "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
    //        "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
    //        "      <tr>\n" +
    //        "         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobgroup") + "</td>\n" +
    //        "         <td width=\"10%\" >" + I18nUtil.getString("jobinfo_field_id") + "</td>\n" +
    //        "         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobname") + "</td>\n" +
    //        "         <td width=\"10%\" >" + I18nUtil.getString("jobconf_monitor_alarm_title") + "</td>\n" +
    //        "         <td width=\"40%\" >" + I18nUtil.getString("jobconf_monitor_alarm_content") + "</td>\n" +
    //        "      </tr>\n" +
    //        "   </thead>\n" +
    //        "   <tbody>\n" +
    //        "      <tr>\n" +
    //        "         <td>{0}</td>\n" +
    //        "         <td>{1}</td>\n" +
    //        "         <td>{2}</td>\n" +
    //        "         <td>" + I18nUtil.getString("jobconf_monitor_alarm_type") + "</td>\n" +
    //        "         <td>{3}</td>\n" +
    //        "      </tr>\n" +
    //        "   </tbody>\n" +
    //        "</table>";

    @Override
    public AlarmResult sendAlarm(XxlAlarmInfo alarmInfo, List<SysUser> noticeUserList, String sendContent) throws AlarmFailException {
        throw new AlarmFailException("邮箱告警暂未实现");
    }


    @Override
    public String buildSendContent(AlarmParam alarmParam) {
        return super.buildSendContent(alarmParam);
    }

    @Override
    public boolean support(JobAlarmEnum jobAlarmEnum) {
        return JobAlarmEnum.EMAIL.equals(jobAlarmEnum);
    }
}
