package com.smartcoding.job.core.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 2017-05-10 20:22:42
 */
public class JobInfoParam implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 执行器名称
     */
    private String appName;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务执行CRON
     */
    private String jobCron;
    /**
     * 执行器任务handler
     */
    private String executorHandler;
    /**
     * 执行器任务参数
     */
    private String executorParam;
    /**
     * 执行器路由策略
     */
    private String executorRouteStrategy;
    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;
    /**
     * 任务执行超时时间，单位秒
     */
    private Integer executorTimeout;
    /**
     * 失败重试次数
     */
    private Integer executorFailRetryCount;
    /**
     * 子任务ID，多个逗号分隔
     */
    private String childJobId;
    /**
     * 任务是否自动开启
     */
    private Boolean autoStartJob;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
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

    public Integer getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(Integer executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public String getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(String childJobId) {
        this.childJobId = childJobId;
    }

    public Boolean getAutoStartJob() {
        return autoStartJob;
    }

    public void setAutoStartJob(Boolean autoStartJob) {
        this.autoStartJob = autoStartJob;
    }

    @Override
    public String toString() {
        return "JobInfoParam{" +
                "appName='" + appName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobCron='" + jobCron + '\'' +
                ", executorHandler='" + executorHandler + '\'' +
                ", executorParam='" + executorParam + '\'' +
                ", executorRouteStrategy='" + executorRouteStrategy + '\'' +
                ", executorBlockStrategy='" + executorBlockStrategy + '\'' +
                ", executorTimeout=" + executorTimeout +
                ", executorFailRetryCount=" + executorFailRetryCount +
                ", childJobId='" + childJobId + '\'' +
                ", autoStartJob=" + autoStartJob +
                '}';
    }
}
