package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务告警记录对象 xxl_job_alarm_log
 *
 * @author wuque
 * @date 2020-08-29
 */
@Data
public class XxlJobAlarmLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 任务日志id
     */
    private Long logId;
    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 告警方式id
     */
    private Integer alarmId;

    /**
     * 告警方式名称
     */
    private String alarmName;

    /**
     * 发送状态:1 成功 0 失败
     */
    private Boolean sendStatus;

    /**
     * 通知人姓名
     */
    private String noticeUsername;

    /**
     * 通知方式的途径
     */
    private String noticeWay;

    /**
     * 失败的原因
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;


}
