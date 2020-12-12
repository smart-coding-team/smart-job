package com.smartcoding.schedule.core.alarm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.schedule.core.exception.AlarmFailException;
import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import com.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import org.apache.commons.text.StringSubstitutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractJobAlarm implements JobAlarm {

    /**
     * 默认告警模板
     */
    private final static String DEFAULT_TEMPLDATE =
            "${date}分布式定时任务平台告警: \n" +
                    "1、告警标题: ${alarmTitle}\n" +
                    "2、告警内容: ${alarmContent}\n" +
                    "3、告警时间: ${alarmTime}\n" +
                    "4、应用名称AppName: ${appName}\n" +
                    "5、任务名称JobName: ${jobName}\n" +
                    "6、任务Id: ${jobId}\n" +
                    "7、任务日志id: ${jobLogId}\n" +
                    "8、触发方式：${triggerTypeEnum}\n" +
                    "9、执行机器：${executorAddress}\n" +
                    "10、执行结果：${handleCode}\n" +
                    "详情请查看${redirectUrl}";
    /**
     * 默认的跳转地址模板
     */
    private final static String DEFAULT_REDIRECT_URL = "http://127.0.0.1:1024/job/jobLog?jobLogId=${jobLogId}";

    public abstract AlarmResult sendAlarm(XxlAlarmInfo alarmInfo, List<SysUser> noticeUserList, String sendContent) throws AlarmFailException;

    @Override
    public AlarmResult sendAlarm(AlarmParam alarmParam) throws AlarmFailException {
        String sendContent = buildSendContent(alarmParam);
        return sendAlarm(alarmParam.getAlarmInfo(), alarmParam.getNoticeUserList(), sendContent);
    }

    public String buildSendContent(AlarmParam alarmParam) {
        XxlAlarmInfo alarmInfo = alarmParam.getAlarmInfo();
        String alarmTemplate = StrUtil.isEmpty(alarmInfo.getAlarmTemplate()) ? DEFAULT_TEMPLDATE : alarmInfo.getAlarmTemplate();
        String redirectUrl = StrUtil.isEmpty(alarmInfo.getRedirectUrl()) ? DEFAULT_REDIRECT_URL : alarmInfo.getRedirectUrl();
        Map<String, Object> valuesMap = new HashMap<>(12);
        TriggerTypeEnum triggerTypeEnum = TriggerTypeEnum.matchCode(alarmParam.getTriggerType(), null);
        valuesMap.put("date", alarmParam.getDate() != null ? alarmParam.getDate() : LocalDate.now());
        valuesMap.put("alarmTitle", alarmParam.getAlarmTitle());
        valuesMap.put("alarmContent", alarmParam.getAlarmContent());
        valuesMap.put("alarmTime", alarmParam.getAlarmTime() != null ? alarmParam.getAlarmTime() : LocalDateTime.now());
        valuesMap.put("appName", alarmParam.getAppName());
        valuesMap.put("jobName", alarmParam.getJobName());
        valuesMap.put("jobId", alarmParam.getJobId());
        valuesMap.put("jobLogId", alarmParam.getJobLogId());
        valuesMap.put("triggerType", triggerTypeEnum != null ? triggerTypeEnum.getTitle() : "");
        valuesMap.put("executorAddress", alarmParam.getExecutorAddress());
        valuesMap.put("handleCode", alarmParam.getHandleCode());
        StringSubstitutor stringSubstitutor = new StringSubstitutor(valuesMap);
        stringSubstitutor.setValueDelimiter(":");
        String fullRedirectUrl = stringSubstitutor.replace(redirectUrl);
        valuesMap.put("redirectUrl", fullRedirectUrl);
        return stringSubstitutor.replace(alarmTemplate);

    }


    protected List<String> getMobileList(List<SysUser> noticeUserList) {
        List<String> mobileList = new ArrayList<>();
        if (CollectionUtil.isEmpty(noticeUserList)) {
            return mobileList;
        }
        for (SysUser sysUser : noticeUserList) {
            String mobile = StrUtil.trimToEmpty(sysUser.getMobile());
            if (!mobile.isEmpty()) {
                mobileList.add(mobile);
            }
        }
        return mobileList;
    }
}
