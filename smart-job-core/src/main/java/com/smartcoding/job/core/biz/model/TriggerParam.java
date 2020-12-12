package com.smartcoding.job.core.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 16/7/22.
 */
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private Long jobId;

    private String jobRequestId;

    private String executorHandler;
    private String executorParams;
    private String executorBlockStrategy;
    private Integer executorTimeout;

    private Long logId;
    private Long logDateTim;

    private String glueType;
    private String glueSource;
    private Long glueUpdateTime;

    private Integer broadcastIndex;
    private Integer broadcastTotal;

    private String requestId = "requestId";

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobRequestId() {
        return jobRequestId;
    }

    public void setJobRequestId(String jobRequestId) {
        this.jobRequestId = jobRequestId;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParams() {
        return executorParams;
    }

    public void setExecutorParams(String executorParams) {
        this.executorParams = executorParams;
    }

    public String getExecutorBlockStrategy() {
        return executorBlockStrategy;
    }

    public void setExecutorBlockStrategy(String executorBlockStrategy) {
        this.executorBlockStrategy = executorBlockStrategy;
    }

    public Integer getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(Integer executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getLogDateTim() {
        return logDateTim;
    }

    public void setLogDateTim(Long logDateTim) {
        this.logDateTim = logDateTim;
    }

    public String getGlueType() {
        return glueType;
    }

    public void setGlueType(String glueType) {
        this.glueType = glueType;
    }

    public String getGlueSource() {
        return glueSource;
    }

    public void setGlueSource(String glueSource) {
        this.glueSource = glueSource;
    }

    public Long getGlueUpdateTime() {
        return glueUpdateTime;
    }

    public void setGlueUpdateTime(Long glueUpdateTime) {
        this.glueUpdateTime = glueUpdateTime;
    }

    public Integer getBroadcastIndex() {
        return broadcastIndex;
    }

    public void setBroadcastIndex(Integer broadcastIndex) {
        this.broadcastIndex = broadcastIndex;
    }

    public Integer getBroadcastTotal() {
        return broadcastTotal;
    }

    public void setBroadcastTotal(Integer broadcastTotal) {
        this.broadcastTotal = broadcastTotal;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "TriggerParam{" +
                "jobId=" + jobId +
                "jobRequestId=" + jobRequestId +
                ", executorHandler='" + executorHandler + '\'' +
                ", executorParams='" + executorParams + '\'' +
                ", executorBlockStrategy='" + executorBlockStrategy + '\'' +
                ", executorTimeout=" + executorTimeout +
                ", logId=" + logId +
                ", logDateTim=" + logDateTim +
                ", requestId=" + requestId +
                ", glueType='" + glueType + '\'' +
                ", glueSource='" + glueSource + '\'' +
                ", glueUpdateTime=" + glueUpdateTime +
                ", broadcastIndex=" + broadcastIndex +
                ", broadcastTotal=" + broadcastTotal +
                '}';
    }

}
