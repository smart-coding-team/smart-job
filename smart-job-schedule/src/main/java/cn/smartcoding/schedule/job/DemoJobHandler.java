

package cn.smartcoding.schedule.job;

import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.handler.annotation.Job;
import cn.smartcoding.job.core.handler.annotation.JobHandler;
import cn.smartcoding.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * 任务Handler示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、继承"IJobHandler"：“IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@Component
@JobHandler
public class DemoJobHandler {

    private Logger log = LoggerFactory.getLogger(DemoJobHandler.class);


    @Job(executorHandler = "demoJobHandler", jobName = "smart-job演示测试任务", jobCron = "0 0 12 * * ?")
    public ReturnT<String> execute(String param) throws Exception {
        log.info("XXL-JOB,demoJobHandler  time:{},param:{}",LocalDateTime.now(),param);
        XxlJobLogger.log("XXL-JOB,demoJobHandler  time:{}", LocalDateTime.now());

        return ReturnT.SUCCESS;
    }

}
