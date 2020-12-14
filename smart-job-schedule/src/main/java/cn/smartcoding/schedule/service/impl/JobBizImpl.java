

package cn.smartcoding.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.job.core.biz.JobBiz;
import cn.smartcoding.job.core.biz.model.JobGroupParam;
import cn.smartcoding.job.core.biz.model.JobInfoParam;
import cn.smartcoding.job.core.biz.model.ReturnT;
import cn.smartcoding.job.core.glue.GlueTypeEnum;
import cn.smartcoding.schedule.core.enums.AddressTypeEnum;
import cn.smartcoding.schedule.core.enums.JobCreateWayEnum;
import cn.smartcoding.schedule.core.model.XxlJobGroup;
import cn.smartcoding.schedule.core.model.XxlJobInfo;
import cn.smartcoding.schedule.core.trigger.TriggerStatusEnum;
import cn.smartcoding.schedule.mapper.XxlJobGroupMapper;
import cn.smartcoding.schedule.service.XxlJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class JobBizImpl implements JobBiz {
    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private XxlJobGroupMapper xxlJobGroupDao;

    @Override
    public ReturnT<String> registryJobGroup(JobGroupParam jobGroupParam) {
        log.info("->>>xxl-job registryJobGroup registryJobGroup:{}", jobGroupParam);
        String appName = StrUtil.trimToNull(jobGroupParam.getAppName());
        String title = StrUtil.trimToNull(jobGroupParam.getTitle());
        if (StringUtils.isEmpty(appName)) {
            ReturnT<String> fail = ReturnT.FAIL;
            log.warn("->>>xxl-job  registryJobGroup, appName is not null");
            fail.setMsg("appName is not null");
            return fail;
        }
        String clientVersion = jobGroupParam.getClientVersion();
        XxlJobGroup xxlJobGroup = xxlJobGroupDao.selectByAppName(appName);
        if (xxlJobGroup == null) {
            xxlJobGroup = new XxlJobGroup();
            xxlJobGroup.setAppName(appName);
            xxlJobGroup.setOrder(1);
            xxlJobGroup.setAddressType(AddressTypeEnum.AUTO.getCode());
            xxlJobGroup.setAlarmStatus(0);
            xxlJobGroup.setTitle(StringUtils.isEmpty(title) ? appName : title);
            xxlJobGroup.setClientVersion(clientVersion);
            if (xxlJobGroupDao.insertSelective(xxlJobGroup) > 0) {
                return ReturnT.SUCCESS;
            } else {
                ReturnT<String> fail = ReturnT.FAIL;
                log.warn("->>>xxl-job registryJobGroup, job group save fail! appName:{}", appName);
                fail.setMsg("job group save fail ");
                return fail;
            }
        } else {
            XxlJobGroup updateXxlJobGroup = new XxlJobGroup();
            updateXxlJobGroup.setId(xxlJobGroup.getId());
            updateXxlJobGroup.setClientVersion(clientVersion);
            xxlJobGroupDao.updateByPrimaryKeySelective(updateXxlJobGroup);
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryJobInfo(JobInfoParam jobInfoParam) {
        log.info("->>>xxl-job registryJobInfo :{}", jobInfoParam);
        String appName = StrUtil.trimToNull(jobInfoParam.getAppName());
        ReturnT<String> returnT = ReturnT.FAIL;
        XxlJobGroup xxlJobGroup = xxlJobGroupDao.selectByAppName(appName);
        if (xxlJobGroup == null) {
            log.warn("->>>xxl-job  registryJobInfo, appName is null,cant not auto create job! appName:{}", appName);
            returnT.setMsg("appName is null,cant not auto create job info,appName:" + appName);
            return returnT;
        }
        //是否重复添加
        XxlJobInfo xxlJobInfo = xxlJobService.getXxxJobInfo(xxlJobGroup.getId(), jobInfoParam.getExecutorHandler());
        if (xxlJobInfo != null && JobCreateWayEnum.MANUAL_CREATE.getCode() == xxlJobInfo.getCreateWay()) {
            ReturnT<String> success = ReturnT.SUCCESS;
            log.info("->>>xxl-job  registryJobInfo, job  is exist and job type is manual create,cant not update job info ! jobName:{},executorHandler:{}", jobInfoParam.getJobName(), jobInfoParam.getExecutorHandler());
            success.setMsg("job info is exist and job type is manual create,cant not update job info,job name:" + jobInfoParam.getJobName() + "executorHandler:" + jobInfoParam.getExecutorHandler());
            return success;
        }

        if (xxlJobInfo != null) {
            Integer triggerStatus = xxlJobInfo.getTriggerStatus();
            BeanUtil.copyProperties(jobInfoParam, xxlJobInfo);
            xxlJobInfo.setGmtCreate(new Date());
            int update = xxlJobService.update(xxlJobInfo);
            if (update > 0 && TriggerStatusEnum.READY.getCode() != triggerStatus && Boolean.TRUE.equals(jobInfoParam.getAutoStartJob())) {
                Long id = xxlJobInfo.getId();
                log.info("->>>xxl-job registryJobInfo,auto start job after create job ! jobId:{}", id);
                xxlJobService.start(id);
            }
            returnT.setMsg("job info update success");
            return returnT;
        } else {
            XxlJobInfo jobInfo = new XxlJobInfo();
            BeanUtil.copyProperties(jobInfoParam, jobInfo);
            jobInfo.setJobGroup(xxlJobGroup.getId());
            jobInfo.setGlueType(GlueTypeEnum.BEAN.getDesc());
            jobInfo.setCreateWay(JobCreateWayEnum.AUTO_CREATE.getCode());
            Long add = xxlJobService.add(jobInfo);
            if (add > 0 && Boolean.TRUE.equals(jobInfoParam.getAutoStartJob())) {
                Long id = Long.valueOf(returnT.getContent());
                log.info("->>>xxl-job registryJobInfo,auto start job after create job ! jobId:{}", id);
                xxlJobService.start(id);
            }
        }
        return returnT;
    }
}
