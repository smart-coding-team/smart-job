

package com.smartcoding.schedule.core.model.bo;

/**
 * @author 无缺
 * @date 2019-12-24
 */
public class XxlShortJobInfoBO {

    /**
     * BIGINT(19) 必填
     *
     */
    private Long id;

    /**
     * VARCHAR(255) 必填
     * 任务名称
     */
    private String jobName;
    /**
     * VARCHAR(255)
     * 执行器任务handler
     */
    private String executorHandler;

    private Long triggerNextTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public Long getTriggerNextTime() {
        return triggerNextTime;
    }

    public void setTriggerNextTime(Long triggerNextTime) {
        this.triggerNextTime = triggerNextTime;
    }
}
