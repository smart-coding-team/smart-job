

package cn.smartcoding.schedule.core.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JobAddressVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * VARCHAR(30) 必填
     * 地址
     */
    private String address;
    /**
     * VARCHAR(64) 必填
     * 在线状态
     */
    private Integer online;
    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public JobAddressVO(String address) {
        this.address = address;
    }

    public JobAddressVO() {
    }

    public JobAddressVO(String address, Integer online, Date updateTime) {
        this.address = address;
        this.online = online;
        this.updateTime = updateTime;
    }


}
