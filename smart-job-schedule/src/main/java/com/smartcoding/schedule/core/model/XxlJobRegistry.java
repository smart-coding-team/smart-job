

package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class XxlJobRegistry implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * BIGINT(19) 必填
     *
     */
    private Long id;

    /**
     * VARCHAR(30) 必填
     * 注册分组
     */
    private String registryGroup;

    /**
     * VARCHAR(64) 必填
     * 注册的key:appName
     */
    private String registryKey;

    /**
     * VARCHAR(64) 必填
     * 注册的值
     */
    private String registryValue;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

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
