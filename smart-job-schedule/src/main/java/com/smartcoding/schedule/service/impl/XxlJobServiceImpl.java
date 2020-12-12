

package com.smartcoding.schedule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.exception.CommonException;
import com.smartcoding.schedule.core.cron.CronExpression;
import com.smartcoding.schedule.core.enums.JobCreateWayEnum;
import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.XxlJobLogReport;
import com.smartcoding.schedule.core.model.bo.*;
import com.smartcoding.schedule.core.model.bo.JobDashbordBO;
import com.smartcoding.schedule.core.model.bo.XxlJobInfoBO;
import com.smartcoding.schedule.core.model.bo.XxlJobStatisticBO;
import com.smartcoding.schedule.core.model.bo.XxlShortJobInfoBO;
import com.smartcoding.schedule.core.route.ExecutorRouteStrategyEnum;
import com.smartcoding.schedule.core.thread.JobScheduleHelper;
import com.smartcoding.schedule.core.trigger.TriggerStatusEnum;
import com.smartcoding.schedule.core.util.I18nUtil;
import com.smartcoding.schedule.mapper.*;
import com.smartcoding.schedule.mapper.*;
import com.smartcoding.schedule.service.XxlJobGroupService;
import com.smartcoding.schedule.service.XxlJobService;
import com.smartcoding.job.core.enums.ExecutorBlockStrategyEnum;
import com.smartcoding.job.core.glue.GlueTypeEnum;
import com.smartcoding.job.core.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * com.smartcoding.robot.core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
    private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

    @Resource
    private XxlJobGroupMapper xxlJobGroupDao;
    @Resource
    private XxlJobInfoMapper xxlJobInfoDao;
    @Resource
    public XxlJobLogMapper xxlJobLogDao;
    @Resource
    private XxlJobLogGlueMapper xxlJobLogGlueDao;
    @Resource
    private XxlJobGroupService xxlJobGroupService;
    @Resource
    private XxlJobLogReportDao xxlJobLogReportDao;


    @Override
    public PageInfo<XxlJobInfoBO> pageList(int pageNum, int pageSize, Long id, Long jobGroup, String glueType, Integer jobStatus, Integer triggerStatus) {
        PageHelper.startPage(pageNum, pageSize);
        // page list
        List<XxlJobInfoBO> list = xxlJobInfoDao.getJobList(id, jobGroup, glueType, jobStatus, triggerStatus);
        Map<Long, XxlJobGroup> xxlJobGroupCache = new HashMap<>();
        Map<Long, Integer> xxlJobGroupOnLineCache = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (XxlJobInfoBO xxlJobInfo : list) {
                Long jobGroupId = xxlJobInfo.getJobGroup();
                XxlJobGroup xxlJobGroup = getXxlJobGroup(xxlJobGroupCache, jobGroupId);
                if (xxlJobGroup != null) {
                    int onLineNum = getOnLineNum(xxlJobGroupOnLineCache, jobGroupId, xxlJobGroup);
                    xxlJobInfo.setOnLineNum(onLineNum);
                    xxlJobInfo.setTitle(xxlJobGroup.getTitle());
                }
            }
        }
        return new PageInfo<XxlJobInfoBO>(list);
    }

    private int getOnLineNum(Map<Long, Integer> xxlJobGroupOnLineCache, Long jobGroupId, XxlJobGroup xxlJobGroup) {
        int onLineNum = 0;
        if (xxlJobGroupOnLineCache.containsKey(jobGroupId)) {
            onLineNum = xxlJobGroupOnLineCache.get(jobGroupId);
        } else {
            String addressList = xxlJobGroup.getAddressList();
            List<String> stringList = StrUtil.split(addressList, ',', true, true);
            onLineNum = xxlJobGroupService.getOnLineNum(stringList, xxlJobGroup.getAppName());
            xxlJobGroupOnLineCache.put(jobGroupId, onLineNum);
        }
        return onLineNum;
    }

    private XxlJobGroup getXxlJobGroup(Map<Long, XxlJobGroup> xxlJobGroupCache, Long jobGroupId) {
        XxlJobGroup xxlJobGroup = null;
        if (xxlJobGroupCache.containsKey(jobGroupId)) {
            xxlJobGroup = xxlJobGroupCache.get(jobGroupId);
        } else {
            xxlJobGroup = xxlJobGroupDao.selectByPrimaryKey(jobGroupId);
            if (xxlJobGroup != null) {
                xxlJobGroupCache.put(jobGroupId, xxlJobGroup);
            }
        }
        return xxlJobGroup;
    }

    @Override
    public Long add(XxlJobInfo jobInfo) {
        // valid
        XxlJobGroup group = xxlJobGroupDao.selectByPrimaryKey(jobInfo.getJobGroup());
        if (group == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_choose") + I18nUtil.getString("jobinfo_field_jobgroup")));
        }
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (jobInfo.getJobName() == null || jobInfo.getJobName().trim().length() == 0) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobname")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_gluetype") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType()) && (jobInfo.getExecutorHandler() == null || jobInfo.getExecutorHandler().trim().length() == 0)) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + "JobHandler"));
        }

        // fix "\r" in shell
        if (GlueTypeEnum.GLUE_SHELL == GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource() != null) {
            jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
        }

        // ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String temp = jobInfo.getChildJobId().trim();
            List<Long> childJobIds = Arrays.stream(StrUtil.splitToLong(temp, ',')).boxed().collect(Collectors.toList());
            for (Long childJobIdItem : childJobIds) {
                XxlJobInfo childJobInfo = xxlJobInfoDao.selectByPrimaryKey(childJobIdItem);
                if (childJobInfo == null) {
                    throw new CommonException(CommonErrorCode.ERROR,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                }
            }
            jobInfo.setChildJobId(temp);
        }
        // add in db
        jobInfo.setTriggerStatus(TriggerStatusEnum.STOPPING.getCode());
        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        xxlJobInfoDao.insertSelective(jobInfo);
        if (jobInfo.getId() < 1) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail")));
        }
        return jobInfo.getId();
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
    public int update(XxlJobInfo jobInfo) {

        // valid
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (jobInfo.getJobName() == null || jobInfo.getJobName().trim().length() == 0) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobname")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }

        // ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String temp = jobInfo.getChildJobId().trim();
            List<Long> childJobIds = Arrays.stream(StrUtil.splitToLong(temp, ',')).boxed().collect(Collectors.toList());
            for (Long childJobIdItem : childJobIds) {
                XxlJobInfo childJobInfo = xxlJobInfoDao.selectByPrimaryKey(childJobIdItem);
                if (childJobInfo == null) {
                    throw new CommonException(CommonErrorCode.ERROR,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                }
            }
            jobInfo.setChildJobId(temp);
        }

        // group valid
        XxlJobGroup jobGroup = xxlJobGroupDao.selectByPrimaryKey(jobInfo.getJobGroup());
        if (jobGroup == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_jobgroup") + I18nUtil.getString("system_unvalid")));
        }

        // stage job info
        XxlJobInfo exists_jobInfo = xxlJobInfoDao.selectByPrimaryKey(jobInfo.getId());
        if (exists_jobInfo == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_not_found")));
        }

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = exists_jobInfo.getTriggerNextTime();
        if (exists_jobInfo.getTriggerStatus().equals(TriggerStatusEnum.READY.getCode()) && !jobInfo.getJobCron().equals(exists_jobInfo.getJobCron())) {
            try {
                Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
                if (nextValidTime == null) {
                    throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_never_fire"));
                }
                nextTriggerTime = nextValidTime.getTime();
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_unvalid") + " | " + e.getMessage());
            }
        }

        exists_jobInfo.setJobGroup(jobInfo.getJobGroup());
        exists_jobInfo.setJobCron(jobInfo.getJobCron());
        exists_jobInfo.setJobName(jobInfo.getJobName());
        exists_jobInfo.setAlarmUserIds(jobInfo.getAlarmUserIds());
        exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
        exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
        exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        exists_jobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        exists_jobInfo.setCreateWay(jobInfo.getCreateWay());
        exists_jobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        exists_jobInfo.setChildJobId(jobInfo.getChildJobId());
        exists_jobInfo.setTriggerNextTime(nextTriggerTime);
        exists_jobInfo.setUpdateTime(new Date());
        return xxlJobInfoDao.updateByPrimaryKeySelective(exists_jobInfo);
    }

    @Override
    public int remove(Long id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.selectByPrimaryKey(id);
        if (xxlJobInfo == null) {
            return 0;
        }
        int ret = xxlJobInfoDao.deleteByPrimaryKey(id);
        xxlJobLogDao.deleteByPrimaryKey(id);
        xxlJobLogGlueDao.deleteByJobId(id);
        return ret;
    }

    @Override
    public int start(Long id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.selectByPrimaryKey(id);

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = new CronExpression(xxlJobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_never_fire"));
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_unvalid") + " | " + e.getMessage());
        }
        XxlJobInfo updateXxlJobInfo = new XxlJobInfo();
        updateXxlJobInfo.setId(xxlJobInfo.getId());
        updateXxlJobInfo.setTriggerStatus(TriggerStatusEnum.READY.getCode());
        updateXxlJobInfo.setTriggerLastTime(0L);
        updateXxlJobInfo.setTriggerNextTime(nextTriggerTime);
        return xxlJobInfoDao.updateByPrimaryKeySelective(updateXxlJobInfo);
    }

    @Override
    public int stop(Long id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.selectByPrimaryKey(id);
        XxlJobInfo updateXxlJobInfo = new XxlJobInfo();
        updateXxlJobInfo.setId(xxlJobInfo.getId());
        updateXxlJobInfo.setTriggerStatus(TriggerStatusEnum.STOPPING.getCode());
        updateXxlJobInfo.setTriggerLastTime(0L);
        updateXxlJobInfo.setTriggerNextTime(0L);
        return xxlJobInfoDao.updateByPrimaryKeySelective(updateXxlJobInfo);
    }

    @Override
    public JobDashbordBO dashboardInfo() {

        int jobInfoCount = xxlJobInfoDao.findAllCount();
        Long jobLogCount = 0L;
        Long jobLogSuccessCount = 0L;
        XxlJobLogReport xxlJobLogReport = xxlJobLogReportDao.queryLogReportTotal();
        if (xxlJobLogReport != null) {
            jobLogCount = xxlJobLogReport.getRunningCount() + xxlJobLogReport.getSucCount() + xxlJobLogReport.getFailCount();
            jobLogSuccessCount = xxlJobLogReport.getSucCount();
        }
        // executor count
        int executorCount = xxlJobGroupDao.count();
        JobDashbordBO jobDashbordBO = new JobDashbordBO();
        jobDashbordBO.setJobInfoCount(jobInfoCount);
        jobDashbordBO.setJobLogCount(jobLogCount);
        jobDashbordBO.setJobLogSuccessCount(jobLogSuccessCount);
        jobDashbordBO.setExecutorCount(executorCount);
        return jobDashbordBO;
    }


    @Override
    public Map<String, Object> chartInfo(Date startDate, Date endDate) {
        // process
        List<String> triggerDayList = new ArrayList<String>();
        List<Long> triggerDayCountRunningList = new ArrayList<Long>();
        List<Long> triggerDayCountSucList = new ArrayList<Long>();
        List<Long> triggerDayCountFailList = new ArrayList<Long>();
        int triggerCountRunningTotal = 0;
        int triggerCountSucTotal = 0;
        int triggerCountFailTotal = 0;

        List<XxlJobLogReport> xxlJobLogReportsList = xxlJobLogReportDao.queryLogReport(startDate, endDate);
        if (xxlJobLogReportsList != null && xxlJobLogReportsList.size() > 0) {
            for (XxlJobLogReport item : xxlJobLogReportsList) {
                String day = cn.hutool.core.date.DateUtil.format(item.getTriggerDay(),"yyyy-MM-dd");
                Long triggerDayCountRunning = item.getRunningCount();
                Long triggerDayCountSuc = item.getSucCount();
                Long triggerDayCountFail = item.getFailCount();

                triggerDayList.add(day);
                triggerDayCountRunningList.add(triggerDayCountRunning);
                triggerDayCountSucList.add(triggerDayCountSuc);
                triggerDayCountFailList.add(triggerDayCountFail);

                triggerCountRunningTotal += triggerDayCountRunning;
                triggerCountSucTotal += triggerDayCountSuc;
                triggerCountFailTotal += triggerDayCountFail;
            }
        } else {
            for (int i = 4; i > -1; i--) {
                triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), -i)));
                triggerDayCountRunningList.add(0L);
                triggerDayCountSucList.add(0L);
                triggerDayCountFailList.add(0L);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("triggerDayList", triggerDayList);
        result.put("triggerDayCountRunningList", triggerDayCountRunningList);
        result.put("triggerDayCountSucList", triggerDayCountSucList);
        result.put("triggerDayCountFailList", triggerDayCountFailList);

        result.put("triggerCountRunningTotal", triggerCountRunningTotal);
        result.put("triggerCountSucTotal", triggerCountSucTotal);
        result.put("triggerCountFailTotal", triggerCountFailTotal);

		/*// set cache
		LocalCacheUtil.set(cacheKey, result, 60*1000);     // cache 60s*/

        return result;
    }

    @Override
    public XxlJobInfo getJobDetialById(Long id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.selectByPrimaryKey(id);
        return xxlJobInfo;
    }

    @Override
    public XxlJobStatisticBO getXxlJobStatisticBO() {
        XxlJobStatisticBO xxlJobStatisticBO = xxlJobInfoDao.getXxlJobStatisticBO();
        return xxlJobStatisticBO;
    }

    @Override
    public List<XxlShortJobInfoBO> getXxlShortJobInfoBO(String searchContent, int pageSize) {
        return xxlJobInfoDao.getXxlShortJobInfoBO(searchContent, pageSize);
    }

    @Override
    public List<XxlShortJobInfoBO> selectJobInfoByIdList(List<Long> jobIdList) {
        if (CollectionUtil.isEmpty(jobIdList)) {
            return new ArrayList<>();
        }
        return xxlJobInfoDao.selectJobInfoByIdList(jobIdList);
    }

    @Override
    public List<XxlShortJobInfoBO> getXxlShortJobInfoBO(int nextMinutes) {
        LocalDateTime now = LocalDateTime.now();
        //未来几分钟
        long nextMilliSeconds = now.plusMinutes(nextMinutes).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return xxlJobInfoDao.getXxlShortJobInfoBOByNextExecute(now.toInstant(ZoneOffset.of("+8")).toEpochMilli(), nextMilliSeconds);
    }

    @Override
    public List<XxlShortJobInfoBO> getXxlShortJobInfoBO(long startSecondsMilli, long endSecondsMilli) {
        return xxlJobInfoDao.getXxlShortJobInfoBOByNextExecute(startSecondsMilli, endSecondsMilli);
    }

    @Override
    public XxlJobInfo getXxxJobInfo(Long jobGroup, String executorHandler) {
        return xxlJobInfoDao.getXxlJobInfo(jobGroup, executorHandler);
    }

    @Override
    public void changeJobCreateWay(Long id, Integer status) {
        JobCreateWayEnum jobCreateWayEnum = JobCreateWayEnum.fromCode(status, JobCreateWayEnum.MANUAL_CREATE);
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(id);
        xxlJobInfo.setCreateWay(jobCreateWayEnum.getCode());
        xxlJobInfoDao.updateByPrimaryKeySelective(xxlJobInfo);
    }

    @Override
    public void changeJobStatus(Long id, Integer status) {
        TriggerStatusEnum triggerStatusEnum = TriggerStatusEnum.fromCode(status, TriggerStatusEnum.STOPPING);
        if (TriggerStatusEnum.READY.equals(triggerStatusEnum)) {
            start(id);
        } else {
            stop(id);
        }

    }
}
