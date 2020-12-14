package cn.smartcoding.job.core.handler.impl;

import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.handler.IJobHandler;
import cn.smartcoding.job.core.log.XxlJobLogger;

/**
 * glue job handler
 *
 * @author xuxueli 2016-5-19 21:05:45
 */
public class GlueJobHandler extends IJobHandler {

    private Long glueUpdateTime;
    private IJobHandler jobHandler;

    public GlueJobHandler(IJobHandler jobHandler, Long glueUpdateTime) {
        this.jobHandler = jobHandler;
        this.glueUpdateTime = glueUpdateTime;
    }

    public Long getGlueUpdateTime() {
        return glueUpdateTime;
    }

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("----------- glue.version:" + glueUpdateTime + " -----------");
        return jobHandler.execute(param);
    }

}
