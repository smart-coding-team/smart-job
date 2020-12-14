package cn.smartcoding.schedule.core.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务告警方式配置对象 xxl_job_alarm_way
 *
 * @author wuque
 * @date 2020-08-29
 */
@Data
public class XxlShortAlarmInfoBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 告警方式
     */
    private String alarmType;

    /**
     * 告警方式名称
     */
    private String alarmName;
    /**
     * 状态:1 启用 0 禁用
     */
    private Integer alarmStatus;


}
