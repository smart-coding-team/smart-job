package com.smartcoding.job.core.handler.annotation;


import com.smartcoding.job.core.enums.ExecutorBlockStrategyEnum;
import com.smartcoding.job.core.enums.ExecutorRouteStrategyEnum;

import java.lang.annotation.*;

/**
 * annotation for job handler
 *
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Job {


    /**
     * 执行器任务handler
     *
     * @return
     */
    String executorHandler();

    /**
     * 任务名称
     *
     * @return
     */
    String jobName();

    /**
     * 任务执行CRON
     *
     * @return
     */
    String jobCron();

    /**
     * 执行器任务参数
     *
     * @return
     */
    String executorParam() default "";

    /**
     * 执行器路由策略
     *
     * @return 默认失败转移
     */
    ExecutorRouteStrategyEnum executorRouteStrategy() default ExecutorRouteStrategyEnum.FAILOVER;

    /**
     * 阻塞处理策略
     *
     * @return
     */
    ExecutorBlockStrategyEnum executorBlockStrategy() default ExecutorBlockStrategyEnum.SERIAL_EXECUTION;

    /**
     * 任务执行超时时间，单位秒
     *
     * @return
     */
    int executorTimeout() default 0;

    /**
     * 失败重试次数
     *
     * @return
     */
    int executorFailRetryCount() default 0;

    /**
     * 子任务ID，多个逗号分隔
     *
     * @return
     */
    String childJobId() default "";

    /**
     * 单任务的创建任务开关
     */
    boolean createJob() default true;

    /**
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";

}
