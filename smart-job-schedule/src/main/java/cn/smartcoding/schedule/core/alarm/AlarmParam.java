package cn.smartcoding.schedule.core.alarm;

import cn.smartcoding.common.core.domain.entity.SysUser;
import cn.smartcoding.schedule.core.model.XxlAlarmInfo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlarmParam implements Serializable {
    private Long jobId;
    private Long jobLogId;
    private String jobName;
    private String alarmContent;
    private String executorAddress;
    private String alarmTitle;
    private Integer handleCode;
    private Integer triggerType;
    private String appName;
    private LocalDateTime alarmTime;
    private LocalDate date;
    private JobAlarmEnum jobAlarmEnum;
    private XxlAlarmInfo alarmInfo;
    private List<SysUser> noticeUserList;

}
