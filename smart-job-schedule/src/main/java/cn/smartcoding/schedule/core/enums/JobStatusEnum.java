

package cn.smartcoding.schedule.core.enums;

/**
 * 任务运行状态
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum JobStatusEnum {
    /**
     * 未开始
     */
    UN_START(0, "未开始"),
    /**
     * 运行中
     */
    RUNNING(1, "运行中"),
    /**
     * 成功
     */
    OK(200, "成功"),
    /**
     * 失败
     */
    FAIL(500, "失败");


    JobStatusEnum(int code, String title) {
        this.code = code;
        this.title = title;
    }

    private int code;
    private String title;

    public String getTitle() {
        return title;
    }

    public int getCode() {
        return code;
    }

    /**
     *
     *  未运行：(-2,0)
     *  运行中：[0,400)
     *  成功：  =400
     *  失败：>=500
     *
     * @param triggerCode
     * @param handlerCode
     * @return
     */

    public static JobStatusEnum getJobStatus(Integer triggerCode, Integer handlerCode) {

        if (triggerCode == null) {
            triggerCode = -1;
        }
        if (handlerCode == null) {
            handlerCode = -1;
        }
        if (triggerCode == 0 && handlerCode == 0) {
            return JobStatusEnum.RUNNING;
        }
        if (triggerCode == 0 && handlerCode == 200) {
            return null;
        }

        if (triggerCode == 200 && handlerCode <200) {
            return JobStatusEnum.RUNNING;
        }
        if (triggerCode == 200 && handlerCode == 200) {

            return JobStatusEnum.OK;
        }
        if ((triggerCode >= 500) || (handlerCode  >= 500)) {

            return JobStatusEnum.FAIL;

        }
        return JobStatusEnum.UN_START;
    }
}
