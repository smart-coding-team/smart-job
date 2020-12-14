

package cn.smartcoding.schedule.core.model.bo;

/**
 * 任务统计
 * @author 无缺
 * @date 2019-08-21
 */
public class XxlJobStatisticBO {

    /**
     * 总的任务数量
     */
    private Integer totalJobNum;
    /**
     * 启用的任务数量
     */
    private Integer enableJobNum;
    /**
     * 禁用的任务数量
     */
    private Integer stopJobNum;
    /**
     * 当前运行中的任务数量
     */
    private Integer runningJobNum;


    public Integer getTotalJobNum() {
        return totalJobNum;
    }

    public void setTotalJobNum(Integer totalJobNum) {
        this.totalJobNum = totalJobNum;
    }

    public Integer getEnableJobNum() {
        return enableJobNum;
    }

    public void setEnableJobNum(Integer enableJobNum) {
        this.enableJobNum = enableJobNum;
    }

    public Integer getStopJobNum() {
        this.stopJobNum=totalJobNum-enableJobNum;
        return stopJobNum;
    }

    public void setStopJobNum(Integer stopJobNum) {
        this.stopJobNum = stopJobNum;
    }

    public Integer getRunningJobNum() {
        return runningJobNum;
    }

    public void setRunningJobNum(Integer runningJobNum) {
        this.runningJobNum = runningJobNum;
    }

}
