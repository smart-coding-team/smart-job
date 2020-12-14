

package cn.smartcoding.schedule.core.model.bo;

import java.io.Serializable;

/**
 * @author 无缺
 * @date 2019-07-12
 */
public class JobDashbordBO implements Serializable {

    /**
     * 任务数量
     */
    private int jobInfoCount;
    /**
     * 调度总次数
     */
    private Long jobLogCount;
    /**
     * 调度成功次数
     */
    private Long jobLogSuccessCount;
    /**
     * 执行器数量
     */
    private int executorCount;

    public int getJobInfoCount() {
        return jobInfoCount;
    }

    public void setJobInfoCount(int jobInfoCount) {
        this.jobInfoCount = jobInfoCount;
    }

    public Long getJobLogCount() {
        return jobLogCount;
    }

    public void setJobLogCount(Long jobLogCount) {
        this.jobLogCount = jobLogCount;
    }

    public Long getJobLogSuccessCount() {
        return jobLogSuccessCount;
    }

    public void setJobLogSuccessCount(Long jobLogSuccessCount) {
        this.jobLogSuccessCount = jobLogSuccessCount;
    }

    public int getExecutorCount() {
        return executorCount;
    }

    public void setExecutorCount(int executorCount) {
        this.executorCount = executorCount;
    }
}
