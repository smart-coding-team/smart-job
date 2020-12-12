

package com.smartcoding.schedule.core.model.bo;

import java.io.Serializable;
import java.util.Date;

public class XxlJobAddressBO implements Serializable {
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
    private Date updateTime;

    public XxlJobAddressBO(String address) {
        this.address = address;
    }

    public XxlJobAddressBO() {
    }

    public XxlJobAddressBO(String address, Integer online, Date updateTime) {
        this.address = address;
        this.online = online;
        this.updateTime = updateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
