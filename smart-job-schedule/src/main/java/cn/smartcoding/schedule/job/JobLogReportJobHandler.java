

package cn.smartcoding.schedule.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.handler.annotation.Job;
import cn.smartcoding.job.core.handler.annotation.JobHandler;
import cn.smartcoding.job.core.log.XxlJobLogger;
import cn.smartcoding.schedule.core.model.XxlJobLogReport;
import cn.smartcoding.schedule.core.model.bo.XxlJobLogReportBO;
import cn.smartcoding.schedule.mapper.XxlJobLogMapper;
import cn.smartcoding.schedule.mapper.XxlJobLogReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * 统计任务执行的数量
 * <p>
 * 开发步骤：
 * 1、继承"IJobHandler"：“IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="JobMethodHandler")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@Component
@JobHandler
public class JobLogReportJobHandler {

    private Logger log = LoggerFactory.getLogger(JobLogReportJobHandler.class);

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;
    @Resource
    private XxlJobLogReportDao xxlJobLogReportDao;

    @Job(executorHandler = "jobLogReport", jobName = "xxljob 统计报表", jobCron = "0 0 0 * * ?")
    public ReturnT<String> execute(String param) throws Exception {
        LocalDate now = LocalDate.now().minusDays(1);
        if (StrUtil.isNotEmpty(param)) {
            now = LocalDate.parse(param);
        }
        reportJobLogByOneDay(now);
        return ReturnT.SUCCESS;
    }

    private void reportJobLogByOneDay(LocalDate triggerDay) {
        LocalDateTime localDateTime = LocalDateTime.of(triggerDay, LocalTime.now());
        LocalDateTime from = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = localDateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        XxlJobLogReportBO xxlJobLogReportBO = xxlJobLogMapper.triggerCountByDay(from, to);
        if (xxlJobLogReportBO != null) {
            XxlJobLogReport xxlJobLogReport = new XxlJobLogReport();
            XxlJobLogReport oldXxlJobLogReport = xxlJobLogReportDao.selectByTriggerDay(triggerDay);
            if (oldXxlJobLogReport != null) {
                xxlJobLogReportDao.deleteById(oldXxlJobLogReport.getId());
            }
            xxlJobLogReport.setTriggerDay(DateUtil.parseDate(triggerDay.toString()));
            long triggerDayCountRunning = covert(xxlJobLogReportBO.getTriggerDayCountRunning());
            long triggerDayCountSuc = covert(xxlJobLogReportBO.getTriggerDayCountSuc());
            long triggerDayCountFail = covert(xxlJobLogReportBO.getTriggerDayCount()) - triggerDayCountRunning - triggerDayCountSuc;
            xxlJobLogReport.setRunningCount(triggerDayCountRunning);
            xxlJobLogReport.setSucCount(triggerDayCountSuc);
            xxlJobLogReport.setFailCount(triggerDayCountFail);
            xxlJobLogReportDao.save(xxlJobLogReport);
        }
        XxlJobLogger.log("XXL-JOB,JobLogReportJob from:{},to:{}", from, to);
    }

    public long covert(Long number) {
        return number == null ? 0 : number;
    }
}
