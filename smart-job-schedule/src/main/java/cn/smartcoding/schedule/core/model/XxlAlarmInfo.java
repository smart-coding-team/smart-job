package cn.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务告警方式配置对象 xxl_job_alarm_way
 *
 * @author wuque
 * @date 2020-08-29
 */
@Data
public class XxlAlarmInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 告警方式英文key
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

    /**
     * 告警模板
     */
    private String alarmTemplate;

    /**
     * 告警地址
     */
    private String alarmUrl;
    /**
     * 网页地址
     */
    private String redirectUrl;


    /**
     * 告警主机地址
     */
    private String alarmHost;

    /**
     * 告警端口
     */
    private String alarmPort;

    /**
     * 告警账号
     */
    private String alarmUsername;

    /**
     * 告警密码
     */
    private String alarmPassword;

    /**
     * 请求的参数
     */
    private String httpParam;

    /**
     * 请求的方式
     */
    private String httpWay;

    /**
     * 请求的headers
     */
    private String httpHeaders;


    /**
     * 连通的状态: 0 未测试,1 成功 2失败
     */
    private Integer connectionStatus;
    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 修改时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;


}
