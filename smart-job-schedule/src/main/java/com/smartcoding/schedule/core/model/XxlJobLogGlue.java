

package com.smartcoding.schedule.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class XxlJobLogGlue implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * BIGINT(19) 必填
     *
     */
    private Long id;

    /**
     * BIGINT(19) 默认值[-1] 必填
     * 任务，主键ID
     */
    private Long jobId;

    /**
     * VARCHAR(50)
     * GLUE类型
     */
    private String glueType;

    /**
     * VARCHAR(128) 默认值[] 必填
     * GLUE备注
     */
    private String glueRemark;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 业务添加时间
     */
    private Date addTime;

    /**
     * TIMESTAMP(19) 默认值[0000-00-00 00:00:00] 必填
     * 业务更新时间
     */
    private Date updateTime;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 修改时间
     */
    private Date gmtModified;

    /**
     * LONGVARCHAR(16777215)
     * GLUE源代码
     */
    private String glueSource;



}
