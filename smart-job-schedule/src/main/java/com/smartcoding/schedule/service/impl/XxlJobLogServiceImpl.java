package com.smartcoding.schedule.service.impl;

import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.exception.CommonException;
import com.smartcoding.schedule.core.conf.XxlJobScheduler;
import com.smartcoding.schedule.core.enums.JobStatusEnum;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.XxlJobLog;
import com.smartcoding.schedule.core.util.I18nUtil;
import com.smartcoding.schedule.mapper.XxlJobInfoMapper;
import com.smartcoding.schedule.mapper.XxlJobLogMapper;
import com.smartcoding.schedule.service.XxlJobLogService;
import com.smartcoding.job.core.biz.ExecutorBiz;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class XxlJobLogServiceImpl implements XxlJobLogService {

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;

    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;

    @Override
    public PageInfo<XxlJobLog> pageList(int pageNum, int pageSize, Long id, Integer jobStatus, Long jobId, Long jobGroup, Date triggerTimeStart, Date triggerTimeEnd) {
        PageHelper.startPage(pageNum, pageSize);
        List<XxlJobLog> jobLogList = xxlJobLogMapper.getJobLogList(id, jobStatus, jobId, jobGroup, triggerTimeStart, triggerTimeEnd);
        return new PageInfo<>(jobLogList);
    }

    @Override
    public XxlJobLog selectByPrimaryKey(Long logId) {
        return xxlJobLogMapper.selectByPrimaryKey(logId);
    }

    @Override
    public void killRunningJob(Long id) {
        // base check
        XxlJobLog jobLog = xxlJobLogMapper.selectByPrimaryKey(id);
        XxlJobInfo jobInfo = xxlJobInfoMapper.selectByPrimaryKey(jobLog.getJobId());
        if (jobInfo == null) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_glue_jobid_invalid"));
        }
        if (ReturnT.SUCCESS_CODE != jobLog.getTriggerCode()) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("joblog_kill_log_limit"));
        }

        // request of kill
        ReturnT<String> runResult;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
            runResult = executorBiz.kill(jobInfo.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(CommonErrorCode.ERROR, e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE != runResult.getCode()) {
            throw new CommonException(CommonErrorCode.ERROR, runResult.getMsg());
        }
        int failCode = ReturnT.FAIL_CODE;
        JobStatusEnum jobStatus = JobStatusEnum.getJobStatus(jobLog.getTriggerCode(), failCode);
        log.info("killjob,jobLogId:{},jobId:{},triggerCode:{},handleCode:{},jobStatus:{}", jobLog.getId(), jobLog.getJobId(), jobLog.getTriggerCode(), failCode, jobStatus);
        XxlJobLog updateXxlJobLog = new XxlJobLog();
        updateXxlJobLog.setId(jobLog.getId());
        if (jobStatus != null) {
            updateXxlJobLog.setJobStatus(jobStatus.getCode());
        }
        updateXxlJobLog.setHandleCode(failCode);
        updateXxlJobLog.setHandleMsg(I18nUtil.getString("joblog_kill_log_byman") + ":" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
        Date handleTime = new Date();
        updateXxlJobLog.setHandleTime(handleTime);
        xxlJobLogMapper.updateByPrimaryKeySelective(updateXxlJobLog);
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(jobInfo.getId());
        xxlJobInfo.setLastJobLogId(jobLog.getId());
        xxlJobInfo.setLastTriggerCode(jobLog.getTriggerCode());
        xxlJobInfo.setLastTriggerTime(jobLog.getTriggerTime());
        xxlJobInfo.setLastHandleCode(failCode);
        xxlJobInfo.setLastHandleTime(handleTime);
        if (jobStatus != null) {
            xxlJobInfo.setJobStatus(jobStatus.getCode());
        }
        xxlJobInfoMapper.updateByPrimaryKeySelective(xxlJobInfo);
    }

    @Override
    public void clearLog(Long jobGroup, Long jobId, Integer type, Integer num) {
        if (type == 1) {
            //删除全部
            xxlJobLogMapper.clearLogAll(jobGroup, jobId);
        } else if (type == 2) {
            // 清理一个月之前日志数据
            Date clearBeforeTime = DateUtil.addMonths(new Date(), -1 * num);
            xxlJobLogMapper.clearLogBeforeTime(jobGroup, jobId, clearBeforeTime);
        } else if (type == 3) {
            xxlJobLogMapper.clearLogByBeforeNum(jobGroup, jobId, num);
        } else {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("joblog_clean_type_invalid"));
        }
    }
}
