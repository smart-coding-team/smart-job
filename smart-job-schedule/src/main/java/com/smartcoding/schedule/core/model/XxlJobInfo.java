

package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XxlJobInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * BIGINT(19) 必填
     *
     */
    private Long id;

    /**
     * BIGINT(19) 必填
     * 执行器主键ID
     */
    private Long jobGroup;

    /**
     * VARCHAR(128) 必填
     * 任务执行CRON
     */
    private String jobCron;

    /**
     * VARCHAR(255) 必填
     * 任务名称
     */
    private String jobName;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 任务状态：0-未运行，1-运行中
     */
    private Integer jobStatus;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 创建方式：0-手动创建，1-自动创建
     */
    private Integer createWay;

    /**
     * TIMESTAMP(19)
     * 任务创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

    /**
     * TIMESTAMP(19)
     * 任务更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * VARCHAR(64)
     * 创建人
     */
    private String author;

    /**
     * VARCHAR(255)
     *  告警人
     */
    private String alarmUserIds;

    /**
     * VARCHAR(50)
     * 执行器路由策略
     */
    private String executorRouteStrategy;

    /**
     * VARCHAR(255)
     * 执行器任务handler
     */
    private String executorHandler;

    /**
     * VARCHAR(512)
     * 执行器任务参数
     */
    private String executorParam;

    /**
     * VARCHAR(50)
     * 阻塞处理策略
     */
    private String executorBlockStrategy;

    /**
     * INTEGER(10) 默认值[0] 必填
     * 任务执行超时时间，单位秒
     */
    private Integer executorTimeout;

    /**
     * INTEGER(10) 默认值[0] 必填
     * 失败重试次数
     */
    private Integer executorFailRetryCount;

    /**
     * VARCHAR(50) 必填
     * GLUE类型
     */
    private String glueType;

    /**
     * VARCHAR(128)
     * GLUE备注
     */
    private String glueRemark;

    /**
     * TIMESTAMP(19)
     * GLUE更新时间
     */
    private Date glueUpdatetime;

    /**
     * VARCHAR(255)
     * 子任务ID，多个逗号分隔
     */
    private String childJobId;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 调度状态：0-停止，1-运行
     */
    private Integer triggerStatus;

    /**
     * BIGINT(19) 默认值[0] 必填
     * 上次调度时间
     */
    private Long triggerLastTime;

    /**
     * BIGINT(19) 默认值[0] 必填
     * 下次调度时间
     */
    private Long triggerNextTime;

    /**
     * BIGINT(19) 默认值[0] 必填
     * 上一次任务执行的日志id
     */
    private Long lastJobLogId;

    /**
     * INTEGER(10) 默认值[0] 必填
     * 上一次调度-结果
     */
    private Integer lastTriggerCode;

    /**
     * TIMESTAMP(19)
     * 上一次调度-时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTriggerTime;

    /**
     * INTEGER(10) 默认值[0] 必填
     * 上一次执行-结果
     */
    private Integer lastHandleCode;

    /**
     * TIMESTAMP(19)
     * 上一次执行-时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastHandleTime;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * LONGVARCHAR(16777215)
     * GLUE源代码
     */
    private String glueSource;


}
