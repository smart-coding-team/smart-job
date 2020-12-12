

package com.smartcoding.schedule.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class XxlJobLogReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date triggerDay;

    private Long runningCount;
    private Long sucCount;

    private Long failCount;

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

}
