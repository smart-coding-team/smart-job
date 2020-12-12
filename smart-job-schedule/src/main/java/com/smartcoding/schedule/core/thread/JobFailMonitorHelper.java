

package com.smartcoding.schedule.core.thread;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.schedule.core.alarm.*;
import com.smartcoding.schedule.core.alarm.*;
import com.smartcoding.schedule.core.conf.XxlJobAdminConfig;
import com.smartcoding.schedule.core.enums.JobAlarmStatusEnum;
import com.smartcoding.schedule.core.enums.JobGroupAlarmStatusEnum;
import com.smartcoding.schedule.core.enums.ThreadTypeEnum;
import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.XxlJobLog;
import com.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import com.smartcoding.schedule.core.util.I18nUtil;
import com.smartcoding.schedule.service.IXxlJobAlarmInfoService;
import com.smartcoding.schedule.service.IXxlJobAlarmLogService;
import com.smartcoding.system.service.ISysUserService;
import com.smartcoding.job.core.biz.model.ReturnT;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * job monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobFailMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);

    private static JobFailMonitorHelper instance = new JobFailMonitorHelper();

    public static JobFailMonitorHelper getInstance() {
        return instance;
    }

    // ---------------------- monitor ----------------------

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("JobFailMonitorHelper").daemon(true).build());
    private ScheduledFuture<?> ringThreadScheduledFuture;
    private AtomicBoolean monitorToStop = new AtomicBoolean(false);
    private Runnable monitor;

    public JobFailMonitorHelper() {
        this.monitor = this::runMonitor;
    }

    public void start() {
        try {
            if (monitorToStop.compareAndSet(false, true)) {
                ringThreadScheduledFuture = executorService.scheduleAtFixedRate(monitor, 1, 10, TimeUnit.SECONDS);
                logger.info(">>>>>>>>>>> xxl-job,Start JobFailMonitorHelper success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start JobFailMonitorHelper failed!", t);
        }
    }


    private void runMonitor() {
        try {
            if (!XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().isOpenThread(ThreadTypeEnum.JOB_FAIL_MONITOR_HELPER)) {
                logger.warn(">>>>>>>>>>> xxl-job, job fail monitor thread is close");
                return;
            }
            List<Long> failTriggerLogIds = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().findTriggerFailJobLogIds(1000);
            List<Long> failHandleLogIds = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().findHandleFailJobLogIds(1000);
            updateAlarmStatus(failTriggerLogIds);
            updateAlarmStatus(failHandleLogIds);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job, job fail monitor thread error:{}", e);
        }
    }

    private void updateAlarmStatus(List<Long> failLogIds) {
        if (failLogIds != null && !failLogIds.isEmpty()) {
            for (Long failLogId : failLogIds) {

                // lock log
                int lockRet = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, 0, -1);
                if (lockRet < 1) {
                    continue;
                }
                XxlJobLog log = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().selectByPrimaryKey(failLogId);
                XxlJobInfo info = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().selectByPrimaryKey(log.getJobId());

                // 1、fail retry monitor
                if (log.getExecutorFailRetryCount() > 0) {
                    JobTriggerPoolHelper.trigger(log.getJobId(), TriggerTypeEnum.RETRY, (log.getExecutorFailRetryCount() - 1), log.getExecutorShardingParam(), log.getExecutorParam());
                    String retryMsg = ">>>>>>>>>>>" + I18nUtil.getString("jobconf_trigger_type_retry") + "<<<<<<<<<<<";
                    XxlJobLog xxlJobLog = new XxlJobLog();
                    xxlJobLog.setId(log.getId());
                    xxlJobLog.setTriggerMsg(log.getTriggerMsg() + retryMsg);
                    XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateByPrimaryKeySelective(xxlJobLog);
                }
                // 2、fail alarm monitor
                // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                JobAlarmStatusEnum jobAlarmStatusEnum = handleJobAlarm(log, info);

                XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, -1, jobAlarmStatusEnum.getCode());
            }
        }
    }

    private JobAlarmStatusEnum handleJobAlarm(XxlJobLog jobLog, XxlJobInfo info) {
        if (info == null || StrUtil.isEmpty(info.getAuthor())) {
            return JobAlarmStatusEnum.DEFAULT;
        }
        XxlJobGroup xxlJobGroup = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().selectByPrimaryKey(info.getJobGroup());
        if (xxlJobGroup == null || !JobGroupAlarmStatusEnum.OPEN.getCode().equals(xxlJobGroup.getAlarmStatus())) {
            return JobAlarmStatusEnum.NO_ALARM;
        }
        List<String> alarmIdList = StrUtil.split(xxlJobGroup.getAlarmIds(), ',', true, true);
        if (CollectionUtil.isEmpty(alarmIdList)) {
            return JobAlarmStatusEnum.NO_ALARM;
        }
        String alarmContent = "";
        String alarmTitle = "";
        if (jobLog.getTriggerCode() != ReturnT.SUCCESS_CODE) {
            alarmContent = jobLog.getTriggerMsg();
            alarmTitle = "调度失败";
        }
        if (jobLog.getHandleCode() > 0 && jobLog.getHandleCode() != ReturnT.SUCCESS_CODE) {
            alarmContent += jobLog.getHandleMsg();
            alarmTitle += "执行失败";
        }
        AlarmResult alarmResult = null;
        AlarmParam alarmParam = new AlarmParam();
        alarmParam.setAppName(xxlJobGroup.getAppName());
        alarmParam.setHandleCode(jobLog.getHandleCode());
        alarmParam.setExecutorAddress(jobLog.getExecutorAddress());
        alarmParam.setTriggerType(jobLog.getTriggerType());
        alarmParam.setJobLogId(jobLog.getJobId());
        alarmParam.setJobId(info.getId());
        alarmParam.setJobName(info.getJobName());
        alarmParam.setAlarmContent(alarmContent);
        alarmParam.setAlarmTitle(alarmTitle);
        String alarmUserIds = info.getAlarmUserIds();
        List<SysUser> sysUserList = getSysUsers(alarmUserIds);

        alarmParam.setNoticeUserList(sysUserList);
        IXxlJobAlarmInfoService xxlJobAlarmInfoService = XxlJobAdminConfig.getAdminConfig().getXxlJobAlarmInfoService();
        Collection<JobAlarm> jobAlarms = XxlJobAdminConfig.getAdminConfig().getJobAlarms();
        IXxlJobAlarmLogService xxlJobAlarmLogService = XxlJobAdminConfig.getAdminConfig().getXxlJobAlarmLogService();
        for (String alarmId : alarmIdList) {
            XxlAlarmInfo xxlAlarmInfo = xxlJobAlarmInfoService.selectXxlJobAlarmInfoById(Long.valueOf(alarmId));
            if (xxlAlarmInfo == null) {
                logger.error(">>>>>>>>>>> xxl-job, xxlAlarminfo is not exist, alarmId:{} ", alarmId);
                continue;
            }
            String alarmType = xxlAlarmInfo.getAlarmType();
            JobAlarmEnum jobAlarmEnum = JobAlarmEnum.match(alarmType, null);
            if (jobAlarmEnum == null) {
                logger.error(">>>>>>>>>>> xxl-job, job  alarm type not support:{} ", alarmType);
                continue;
            }
            alarmParam.setJobAlarmEnum(jobAlarmEnum);
            alarmParam.setAlarmInfo(xxlAlarmInfo);
            AlarmManager alarmProviderManager = new AlarmProviderManager(jobAlarms, xxlJobAlarmLogService);
            alarmResult = alarmProviderManager.sendAlarm(alarmParam);
        }
        return alarmResult != null && alarmResult.getSendStatus() ? JobAlarmStatusEnum.OK : JobAlarmStatusEnum.FAIL;
    }


    private List<SysUser> getSysUsers(String alarmUserIds) {
        try {
            ISysUserService sysUserService = XxlJobAdminConfig.getAdminConfig().getSysUserService();
            return sysUserService.selectUserByIds(alarmUserIds);
        } catch (Exception e) {
            logger.warn(">>>>>>>>>>> xxl-job, JobFailMonitorHelper get notice user fail", e);
            return null;
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, JobFailMonitorHelper stopping");
            if (monitorToStop.compareAndSet(true, false)) {
                ringThreadScheduledFuture.cancel(true);
                logger.info("Stop JobFailMonitorHelper success!");
            }
            XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().autoDeleteOpenThread(ThreadTypeEnum.JOB_FAIL_MONITOR_HELPER);
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,stop  JobFailMonitorHelper   failed!", t);
        }
    }

}
