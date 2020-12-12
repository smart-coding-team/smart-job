

package com.smartcoding.schedule.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.schedule.mapper.XxlJobLogMapper;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.handler.annotation.Job;
import com.smartcoding.job.core.handler.annotation.JobHandler;
import com.smartcoding.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
public class DeleteJobLogJobHandler {

    private Logger log = LoggerFactory.getLogger(DeleteJobLogJobHandler.class);

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;
    //默认日志保留最近7天，其他归档
    private final static int BEFORE_DAYS = 7;
    //分页的数量
    private final static int PAGE_SIZE = 500;

    @Job(executorHandler = "deleteJobLogJobHandler", jobName = "定时删除xxl-job调度日志", jobCron = "0 0 12 * * ?")
    public ReturnT<String> execute(String param) throws Exception {
        LocalDate beforeDaysDate = LocalDate.now().minusDays(BEFORE_DAYS);
        if (StrUtil.isNotEmpty(param)) {
            beforeDaysDate = LocalDate.parse(param);
        }
        int totalNum = xxlJobLogMapper.countXxlJobLog(beforeDaysDate);
        int totalPage = totalNum % PAGE_SIZE > 0 ? totalNum / PAGE_SIZE + 1 : totalNum / PAGE_SIZE;
        XxlJobLogger.log("XXL-JOB,DeleteJobLogJobHandler  running,jobLog move to jobLogHistory,beforeDaysDate:{},totalNum:{},totalPage:{}", beforeDaysDate, totalNum, totalPage);
        AtomicInteger successNum = new AtomicInteger();
        for (int i = 0; i < totalPage; i++) {
            log.debug("XXL-JOB,DeleteJobLogJobHandler,running pageNo:{},pageSize:{}", i, PAGE_SIZE);
            List<Long> list = xxlJobLogMapper.getXxlJobLogListByGmtCreate(beforeDaysDate, PAGE_SIZE);
            if (CollectionUtil.isEmpty(list)) {
                continue;
            }
            list.forEach(xxlJobLogId -> {
                // job log delete
                xxlJobLogMapper.deleteByPrimaryKey(xxlJobLogId);
                successNum.getAndIncrement();
            });
        }
        XxlJobLogger.log("XXL-JOB,DeleteJobLogJobHandler totalNum:{},successNum:{}", totalNum, successNum);
        return ReturnT.SUCCESS;
    }

}
