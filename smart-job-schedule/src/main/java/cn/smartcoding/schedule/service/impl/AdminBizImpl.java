

package cn.smartcoding.schedule.service.impl;

import cn.smartcoding.job.core.biz.AdminBiz;
import cn.smartcoding.job.core.biz.model.HandleCallbackParam;
import cn.smartcoding.job.core.biz.model.RegistryParam;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.handler.IJobHandler;
import cn.smartcoding.schedule.core.model.XxlJobInfo;
import cn.smartcoding.schedule.core.model.XxlJobLog;
import cn.smartcoding.schedule.core.thread.JobTriggerPoolHelper;
import cn.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import cn.smartcoding.schedule.core.util.I18nUtil;
import cn.smartcoding.schedule.mapper.XxlJobInfoMapper;
import cn.smartcoding.schedule.mapper.XxlJobLogMapper;
import cn.smartcoding.schedule.mapper.XxlJobRegistryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public XxlJobLogMapper xxlJobLogDao;
    @Resource
    private XxlJobInfoMapper xxlJobInfoDao;
    @Resource
    private XxlJobRegistryMapper xxlJobRegistryDao;


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam : callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.debug(">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode() == IJobHandler.SUCCESS.getCode() ? "success" : "fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid jobLog item
        XxlJobLog jobLog = xxlJobLogDao.selectByPrimaryKey(handleCallbackParam.getLogId());
        if (jobLog == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "jobLog item not found.");
        }
        if (jobLog.getHandleCode() > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "jobLog repeate callback.");     // avoid repeat callback, trigger child job etc
        }

        // trigger success, to trigger child job
        String callbackMsg = null;
        int handleCode = handleCallbackParam.getExecuteResult().getCode();
        if (IJobHandler.SUCCESS.getCode() == handleCode) {
            XxlJobInfo xxlJobInfo = xxlJobInfoDao.selectByPrimaryKey(jobLog.getJobId());
            if (xxlJobInfo != null && xxlJobInfo.getChildJobId() != null && xxlJobInfo.getChildJobId().trim().length() > 0) {
                callbackMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>" + I18nUtil.getString("jobconf_trigger_child_run") + "<<<<<<<<<<< </span><br>";

                String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    Long childJobId = (childJobIds[i] != null && childJobIds[i].trim().length() > 0 && isNumeric(childJobIds[i])) ? Long.valueOf(childJobIds[i]) : -1L;
                    if (childJobId > 0) {

                        JobTriggerPoolHelper.trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.SUCCESS;

                        // add msg
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i + 1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.getCode() == ReturnT.SUCCESS_CODE ? I18nUtil.getString("system_success") : I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i + 1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (jobLog.getHandleMsg() != null) {
            handleMsg.append(jobLog.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (callbackMsg != null) {
            handleMsg.append(callbackMsg);
        }
        XxlJobLog xxlJobLog = xxlJobLogDao.selectByPrimaryKey(handleCallbackParam.getLogId());
        if (xxlJobLog == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "jobLog item not found.");
        }

        // success, save jobLog
        XxlJobLog updateXxlJobLog = new XxlJobLog();
        Date handleTime = new Date();
        updateXxlJobLog.setId(jobLog.getId());
        updateXxlJobLog.setHandleTime(handleTime);
        updateXxlJobLog.setHandleCode(handleCode);
        String str = handleMsg.toString();
        if (str.length() > 500) {
            str = str.substring(500);
        }
        updateXxlJobLog.setHandleMsg(str);
        xxlJobLogDao.updateByPrimaryKeySelective(updateXxlJobLog);
        xxlJobLogDao.updateJobStatusByHandleCode(jobLog.getId(), handleCode);
        XxlJobLog oldXxlJobLog = xxlJobLogDao.selectByPrimaryKey(jobLog.getId());
        logger.info("JobApiController.callback,jobLogId:{},jobId:{},triggerCode:{},handleCode:{},jobStatus:{}", jobLog.getId(), jobLog.getJobId(), xxlJobLog.getTriggerCode(), handleCode, oldXxlJobLog.getJobStatus());
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(jobLog.getJobId());
        xxlJobInfo.setLastJobLogId(jobLog.getId());
        xxlJobInfo.setLastTriggerCode(xxlJobLog.getTriggerCode());
        xxlJobInfo.setLastTriggerTime(xxlJobLog.getTriggerTime());
        xxlJobInfo.setLastHandleTime(handleTime);
        xxlJobInfo.setLastHandleCode(handleCode);
        //调度成功
        xxlJobInfo.setJobStatus(oldXxlJobLog.getJobStatus());
        xxlJobInfoDao.updateByPrimaryKeySelective(xxlJobInfo);
        return ReturnT.SUCCESS;
    }

    private boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        //开启自动注册
        int ret = xxlJobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret < 1) {
            xxlJobRegistryDao.registrySave(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        xxlJobRegistryDao.registryDelete(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        return ReturnT.SUCCESS;
    }

}
