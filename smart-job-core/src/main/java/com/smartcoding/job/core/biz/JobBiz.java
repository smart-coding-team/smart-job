package com.smartcoding.job.core.biz;

import com.smartcoding.job.core.biz.model.JobGroupParam;
import com.smartcoding.job.core.biz.model.JobInfoParam;
import com.smartcoding.job.core.biz.model.ReturnT;

/**
 * @author xuxueli 2017-07-27 21:52:49
 */
public interface JobBiz {

    // ---------------------- registry ----------------------

    /**
     * registry
     *
     * @param jobGroupParam
     * @return
     */
    ReturnT<String> registryJobGroup(JobGroupParam jobGroupParam);

    /**
     * registry remove
     *
     * @param jobInfoParam
     * @return
     */
    ReturnT<String> registryJobInfo(JobInfoParam jobInfoParam);

}
