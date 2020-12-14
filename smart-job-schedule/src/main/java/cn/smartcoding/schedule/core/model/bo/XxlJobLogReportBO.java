

package cn.smartcoding.schedule.core.model.bo;


public class XxlJobLogReportBO {

    private Long triggerDayCount;
    private Long triggerDayCountRunning;
    private Long triggerDayCountSuc;

    public Long getTriggerDayCount() {
        return triggerDayCount;
    }

    public void setTriggerDayCount(Long triggerDayCount) {
        this.triggerDayCount = triggerDayCount;
    }

    public Long getTriggerDayCountRunning() {
        return triggerDayCountRunning;
    }

    public void setTriggerDayCountRunning(Long triggerDayCountRunning) {
        this.triggerDayCountRunning = triggerDayCountRunning;
    }

    public Long getTriggerDayCountSuc() {
        return triggerDayCountSuc;
    }

    public void setTriggerDayCountSuc(Long triggerDayCountSuc) {
        this.triggerDayCountSuc = triggerDayCountSuc;
    }
}
