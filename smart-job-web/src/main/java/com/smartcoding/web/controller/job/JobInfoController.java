package com.smartcoding.web.controller.job;


import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.exception.CommonException;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.common.utils.SecurityUtils;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.bo.XxlShortJobInfoBO;
import com.smartcoding.schedule.core.thread.JobTriggerPoolHelper;
import com.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import com.smartcoding.schedule.core.util.I18nUtil;
import com.smartcoding.schedule.dto.TriggerJobDto;
import com.smartcoding.schedule.service.XxlJobService;
import com.smartcoding.job.core.cron.CronExpression;
import com.smartcoding.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(tags = "任务配置接口")
@RestController
@RequestMapping("/api/job")
public class JobInfoController {

    @Resource
    private XxlJobService jobService;

    /**
     * 查询任务列表
     */
    @GetMapping("/pageList")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:list')")
    public ResultModel pageList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "jobGroup", required = false) Long jobGroup,
                                @RequestParam(value = "glueType", required = false) String glueType,
                                @RequestParam(value = "jobStatus", required = false) Integer jobStatus,
                                @RequestParam(value = "triggerStatus", required = false) Integer triggerStatus) {
        return ResultModel.success(jobService.pageList(pageNum, pageSize, id, jobGroup, glueType, jobStatus, triggerStatus));
    }

    /**
     * 新增任务
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:add')")
    @Log(title = "新增任务", businessType = BusinessType.INSERT)
    public ResultModel add(HttpServletRequest request, @RequestBody XxlJobInfo jobInfo) {
        jobInfo.setAuthor(SecurityUtils.getUsername());
        return ResultModel.success(jobService.add(jobInfo));
    }

    /**
     * 新增任务
     */
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:update')")
    @Log(title = "更新任务", businessType = BusinessType.UPDATE)
    public ResultModel update(HttpServletRequest request, @RequestBody XxlJobInfo jobInfo) {
        return ResultModel.success(jobService.update(jobInfo));
    }

    /**
     * 删除任务
     */
    @PostMapping(value = "/remove/{id}")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:remove')")
    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    public ResultModel remove(@PathVariable(value = "id") Long id) {
        return ResultModel.success(jobService.remove(id));
    }

    /**
     * 任务状态改变
     */
    @RequestMapping(value = "/changeJobStatus", method = RequestMethod.POST)
    @PreAuthorize("@ss.hasPermi('job:jobInfo:changeJobStatus')")
    @Log(title = "任务状态修改", businessType = BusinessType.UPDATE)
    public ResultModel changeJobStatus(Long id, Integer status) {
        jobService.changeJobStatus(id, status);
        return ResultModel.success();
    }

    /**
     * 任务创建方式改变
     */
    @RequestMapping(value = "/changeJobCreateWay", method = RequestMethod.POST)
    @PreAuthorize("@ss.hasPermi('job:jobInfo:changeJobCreateWay')")
    @Log(title = "任务创建方式改变", businessType = BusinessType.UPDATE)
    public ResultModel changeJobCreateWay(Long id, Integer status) {
        jobService.changeJobCreateWay(id, status);
        return ResultModel.success();
    }

    /**
     * 触发任务
     */
    @PostMapping(value = "/trigger")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:trigger')")
    @Log(title = "触发任务", businessType = BusinessType.RUN)
    public ResultModel triggerJob(@RequestBody TriggerJobDto dto) {
        // force cover job param
        String executorParam = dto.getExecutorParam();
        if (executorParam == null) {
            executorParam = "";
        }
        JobTriggerPoolHelper.trigger(dto.getJobId(), TriggerTypeEnum.MANUAL, -1, null, executorParam, dto.getAddress());
        return ResultModel.success();
    }

    /**
     * 获取近5次触发时间
     */
    @GetMapping("/nextTriggerTime")
    public ResultModel<List<String>> nextTriggerTime(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = cronExpression.getNextValidTimeAfter(lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_field_cron_invalid"));
        }
        return ResultModel.success(result);
    }

    /**
     * 加载任务详情
     */
    @GetMapping("/loadById")
    @PreAuthorize("@ss.hasPermi('job:jobInfo:detail')")
    public ResultModel<XxlJobInfo> loadById(@RequestParam("id") Long id) {
        return ResultModel.success(jobService.getJobDetialById(id));
    }

    /**
     * 搜索任务列表
     */
    @GetMapping("/querySearch")
    public ResultModel querySearch(@RequestParam(value = "queryStr", required = false) String searchContent, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<XxlShortJobInfoBO> list = jobService.getXxlShortJobInfoBO(searchContent, pageSize);
        return ResultModel.success(list);
    }

    /**
     * 查询任务根据任务id
     */
    @GetMapping("/queryIds")
    public ResultModel queryIds(@RequestParam(value = "jobIds") String jobIds) {
        List<Long> jobIdList = Arrays.stream(StrUtil.splitToLong(jobIds, ',')).boxed().collect(Collectors.toList());
        List<XxlShortJobInfoBO> list = jobService.selectJobInfoByIdList(jobIdList);
        return ResultModel.success(list);
    }
}
