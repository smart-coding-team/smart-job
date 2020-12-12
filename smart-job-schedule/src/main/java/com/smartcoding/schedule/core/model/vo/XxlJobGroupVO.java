

package com.smartcoding.schedule.core.model.vo;

import java.io.Serializable;

/**
 * @author 无缺
 * @date 2019-07-13
 */
public class XxlJobGroupVO implements Serializable {
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
     * 告警地址
     */
    private String alarmUrl;
    /**
     * VARCHAR(250) 默认值[]
     * 告警地址
     */
    private String wxAlarmUrl;
    /**
     * 客户版本
     */
    private String clientVersion;
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getAlarmUrl() {
        return alarmUrl;
    }

    public void setAlarmUrl(String alarmUrl) {
        this.alarmUrl = alarmUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWxAlarmUrl() {
        return wxAlarmUrl;
    }

    public void setWxAlarmUrl(String wxAlarmUrl) {
        this.wxAlarmUrl = wxAlarmUrl;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
}
