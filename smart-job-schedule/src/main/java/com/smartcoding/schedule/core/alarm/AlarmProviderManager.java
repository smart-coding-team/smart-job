package com.smartcoding.schedule.core.alarm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.schedule.core.exception.AlarmFailException;
import com.smartcoding.schedule.core.model.XxlJobAlarmLog;
import com.smartcoding.schedule.service.IXxlJobAlarmLogService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AlarmProviderManager implements AlarmManager {

    private Collection<JobAlarm> jobAlarms = Collections.emptyList();
    private IXxlJobAlarmLogService xxlJobAlarmLogService;


    public AlarmProviderManager(Collection<JobAlarm> jobAlarms, IXxlJobAlarmLogService xxlJobAlarmLogService) {
        Assert.notNull(jobAlarms, "jobAlarms list cannot be null");
        this.jobAlarms = jobAlarms;
        this.xxlJobAlarmLogService = xxlJobAlarmLogService;
    }
    public AlarmProviderManager(Collection<JobAlarm> jobAlarms) {
        Assert.notNull(jobAlarms, "jobAlarms list cannot be null");
        this.jobAlarms = jobAlarms;
    }
    public AlarmResult sendAlarm(AlarmParam alarmParam, boolean saveAlarmLog) {
        Collection<JobAlarm> jobAlarms = getJobAlarms();
        AlarmResult alarmResult = null;
        for (JobAlarm jobAlarm : jobAlarms) {
            if (!jobAlarm.support(alarmParam.getJobAlarmEnum())) {
                continue;
            }
            try {
                alarmResult = jobAlarm.sendAlarm(alarmParam);

            } catch (AlarmFailException e) {
                log.error(">>>>>>>>>>> xxl-job, job fail send alarm error,JobLogId={},errorMsg={}", alarmParam.getJobLogId(), e.getMessage(), e);
                alarmResult = new DefaultFailAlarmResult(e.getMessage());

            }
            if (saveAlarmLog && xxlJobAlarmLogService != null) {
                XxlJobAlarmLog xxlJobAlarmLog = buildAlarmLog(alarmParam, alarmResult);
                xxlJobAlarmLogService.insertXxlJobAlarmLog(xxlJobAlarmLog);
            }

        }
        return alarmResult;
    }

    @Override
    public AlarmResult sendAlarm(AlarmParam alarmParam) {
        return sendAlarm(alarmParam, true);
    }

    @Override
    public AlarmResult testSendAlarm(AlarmParam alarmParam) {
        return sendAlarm(alarmParam, false);
    }

    private XxlJobAlarmLog buildAlarmLog(AlarmParam alarmParam, AlarmResult alarmResult) {
        List<SysUser> noticeUserList = alarmParam.getNoticeUserList();
        String noticeUsers = CollectionUtil.isNotEmpty(noticeUserList) ? noticeUserList.stream().map(SysUser::getUserName).collect(Collectors.joining(",")) : "";
        XxlJobAlarmLog xxlJobAlarmLog = new XxlJobAlarmLog();
        xxlJobAlarmLog.setAlarmId(alarmParam.getAlarmInfo().getId());
        xxlJobAlarmLog.setAlarmName(alarmParam.getAlarmInfo().getAlarmName());
        String alarmType = alarmParam.getAlarmInfo().getAlarmType();
        JobAlarmEnum jobAlarmEnum = JobAlarmEnum.match(alarmType, null);
        xxlJobAlarmLog.setNoticeWay(jobAlarmEnum!=null?jobAlarmEnum.getTitle():"");
        xxlJobAlarmLog.setLogId(alarmParam.getJobLogId());
        xxlJobAlarmLog.setJobName(alarmParam.getJobName());
        xxlJobAlarmLog.setJobId(alarmParam.getJobId());
        xxlJobAlarmLog.setNoticeUsername(noticeUsers);
        xxlJobAlarmLog.setSendStatus(alarmResult.getSendStatus());
        xxlJobAlarmLog.setErrorMsg(StrUtil.sub(alarmResult.getErrorMsg(), 0, 300));
        xxlJobAlarmLog.setGmtCreate(alarmResult.getSendDate());
        xxlJobAlarmLog.setGmtModified(new Date());
        return xxlJobAlarmLog;
    }

    public Collection<JobAlarm> getJobAlarms() {
        return jobAlarms;
    }

    public void setJobAlarms(List<JobAlarm> jobAlarms) {
        Assert.notNull(jobAlarms, "jobAlarms list cannot be null");
        this.jobAlarms = jobAlarms;
    }

    public IXxlJobAlarmLogService getXxlJobAlarmLogService() {
        return xxlJobAlarmLogService;
    }

    public void setXxlJobAlarmLogService(IXxlJobAlarmLogService xxlJobAlarmLogService) {
        Assert.notNull(xxlJobAlarmLogService, "xxlJobAlarmLogService  cannot be null");
        this.xxlJobAlarmLogService = xxlJobAlarmLogService;
    }


}
