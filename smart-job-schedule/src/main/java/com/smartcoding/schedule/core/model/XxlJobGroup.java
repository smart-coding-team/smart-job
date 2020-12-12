

package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XxlJobGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * BIGINT(19) 必填
     */
    private Long id;

    /**
     * VARCHAR(64) 必填
     * 执行器AppName
     */
    private String appName;

    /**
     * VARCHAR(60) 必填
     * 执行器名称
     */
    private String title;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 排序
     */
    private Integer order;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private Integer addressType;

    /**
     * VARCHAR(512)
     * 执行器地址列表，多地址逗号分隔
     */
    private String addressList;

    /**
     * TINYINT(3) 默认值[0] 必填
     * 告警状态:0 关闭 1开启
     */
    private Integer alarmStatus;

    /**
     * VARCHAR(250) 默认值[]
     * 报警id
     */
    private String alarmIds;

    /**
     * VARCHAR(250) 默认值[]
     * 客户端版本
     */
    private String clientVersion;

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


}
