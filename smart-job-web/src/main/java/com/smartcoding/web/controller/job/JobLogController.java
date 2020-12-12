package com.smartcoding.web.controller.job;

import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.schedule.core.conf.XxlJobScheduler;
import com.smartcoding.schedule.core.model.XxlJobLog;
import com.smartcoding.schedule.service.XxlJobLogService;
import com.smartcoding.job.core.biz.ExecutorBiz;
import com.smartcoding.job.core.biz.model.LogResult;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.util.DateUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/log")
@Api(tags = "任务运行日志接口")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    public XxlJobLogService jobLogService;

    /**
     * 运行日志列表
     */
    @GetMapping("/pageList")
    @PreAuthorize("@ss.hasPermi('job:jobLog:list')")
    public ResultModel pageList(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "jobStatus", required = false) Integer jobStatus,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "jobGroup", required = false) Long jobGroup,
            @RequestParam(value = "triggerTimeStart", required = false) String triggerTimeStartStr,
            @RequestParam(value = "triggerTimeEnd", required = false) String triggerTimeEndStr) {
        // parse param
        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        if (StrUtil.isNotEmpty(triggerTimeStartStr)) {
            triggerTimeStart = DateUtil.parseDateTime(triggerTimeStartStr);
        }
        if (StrUtil.isNotEmpty(triggerTimeEndStr)) {
            triggerTimeEnd = DateUtil.parseDateTime(triggerTimeEndStr);
        }
        PageInfo<XxlJobLog> simplePageInfo = jobLogService.pageList(pageNum, pageSize,id, jobStatus, jobId, jobGroup, triggerTimeStart, triggerTimeEnd);
        return ResultModel.success(simplePageInfo);
    }

    /**
     * 运行日志详情
     */
    @RequestMapping(value = "/logDetailCat", method = RequestMethod.GET)
    @PreAuthorize("@ss.hasPermi('job:jobLog:detail')")
    public ResultModel logDetailCat(@RequestParam(value = "executorAddress")String executorAddress,@RequestParam(value = "triggerTime") Long triggerTime, @RequestParam(value = "logId")Long logId, @RequestParam(value = "fromLineNum")Integer fromLineNum) {
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(executorAddress);
            //TODO 支持指定读取的行数,避免读取文件过大,造成接口问题
            ReturnT<LogResult> returnT = executorBiz.log(triggerTime, logId, fromLineNum);
            // is end
            LogResult logResult = returnT.getContent();
            if (logResult != null && fromLineNum > logResult.getToLineNum()) {
                XxlJobLog jobLog = jobLogService.selectByPrimaryKey(logId);
                if (jobLog.getHandleCode() > 0) {
                    logResult.setEnd(true);
                }
            }
            return ResultModel.success(logResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.fail(CommonErrorCode.ERROR, e.getMessage());
        }
    }

    /**
     * 任务终止
     */
    @RequestMapping(value = "/killJob/{id}", method = RequestMethod.POST)
    @PreAuthorize("@ss.hasPermi('job:jobLog:killJob')")
    @Log(title = "任务终止", businessType = BusinessType.RUN)
    public ResultModel logKill(@PathVariable(value = "id") Long id) {
        jobLogService.killRunningJob(id);
        return ResultModel.success();
    }

    /**
     * 清理任务日志
     */
    @PostMapping("/clearLog")
    @PreAuthorize("@ss.hasPermi('job:jobLog:clearLog')")
    @Log(title = "清理任务日志", businessType = BusinessType.DELETE)
    public ReturnT<String> clearLog(@RequestParam(value = "jobGroup") Long jobGroup, @RequestParam(value = "jobId") Long jobId, @RequestParam(value = "type") Integer type, @RequestParam(value = "num", required = false) Integer num) {
        jobLogService.clearLog(jobGroup, jobId, type, num);
        return ReturnT.SUCCESS;
    }
}
