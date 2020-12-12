package com.smartcoding.job.core.handler;

import com.smartcoding.job.core.biz.model.ReturnT;

import java.lang.reflect.InvocationTargetException;

/**
 * job handler
 *
 * @author xuxueli 2015-12-19 19:06:38
 */
public abstract class IJobHandler {


    /**
     * success
     */
    public static final ReturnT<String> SUCCESS = new ReturnT<String>(200, "success");
    /**
     * fail
     */
    public static final ReturnT<String> FAIL = new ReturnT<String>(500, "fail");
    /**
     * fail timeout
     */
    public static final ReturnT<String> FAIL_TIMEOUT = new ReturnT<String>(502, "fail timeout");


    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @param param
     * @return
     * @throws Exception
     */
    public abstract ReturnT<String> execute(String param) throws Exception;

    /**
     * init handler, invoked when JobThread init
     */
    public void init() throws InvocationTargetException, IllegalAccessException {
        // do something
    }


    /**
     * destroy handler, invoked when JobThread destroy
     */
    public void destroy() throws InvocationTargetException, IllegalAccessException {
        // do something
    }


}
