

package cn.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class XxlJobLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * INTEGER(10) 必填
     *
     */
    private Long id;

    /**
     * VARCHAR(60) 必填
     * 任务调用id
     */
    private String jobRequestId;

    /**
     * VARCHAR(255) 必填
     * 任务名称
     */
    private String jobName;

    /**
     * INTEGER(10) 必填
     * 执行器主键ID
     */
    private Long jobGroup;

    /**
     * INTEGER(10) 必填
     * 任务，主键ID
     */
    private Long jobId;

    /**
     * VARCHAR(255)
     * 执行器地址，本次执行的地址
     */
    private String executorAddress;


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
     * VARCHAR(20)
     * 执行器任务分片参数，格式如 1/2
     */
    private String executorShardingParam;

    /**
     * INTEGER(10) 默认值[0] 必填
     * 失败重试次数
     */
    private Integer executorFailRetryCount;


    /**
     * TIMESTAMP(19)
     * 调度-时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date triggerTime;

    /**
     * VARCHAR(255)
     * 调度地址，本次调度的地址
     */
    private String triggerAddress;

    /**
     * INTEGER(10) 必填
     * 调度-结果
     */
    private Integer triggerCode;

    /**
     * VARCHAR(512)
     * 触发方式：0-手动执行 1-cron 2-失败重试触发
     *
     */
    private Integer triggerType;

    /**
     * TIMESTAMP(19)
     * 执行-时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;

    /**
     * INTEGER(10) 必填
     * 执行-状态
     */
    private Integer handleCode;


    /**
     * INTEGER(10) 必填
     * 任务总结果: 0 未开始 1:运行中  200 成功  500 失败
     */
    private Integer jobStatus;

    /**
     *
     * TINYINT(3) 默认值[0] 必填
     * 告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败
     */
    private Integer alarmStatus;

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
     * LONGVARCHAR(65535)
     * 调度-日志
     */
    private String triggerMsg;

    /**
     * LONGVARCHAR(65535)
     * 执行-日志
     */
    private String handleMsg;




}
