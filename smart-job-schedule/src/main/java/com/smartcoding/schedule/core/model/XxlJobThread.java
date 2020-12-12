package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务线程对象 xxl_job_schedule
 *
 * @author wuque
 * @date 2020-08-29
 */
@Data
public class XxlJobThread implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 机器地址
     */
    private String address;

    /**
     * 机器名称
     */
    private String hostName;

    /**
     * 线程类型
     */
    private Integer threadType;

    /**
     * 状态:1 启用 0 禁用
     */
    private Integer threadStatus;

    /**
     * 上次在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOnlineTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


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
