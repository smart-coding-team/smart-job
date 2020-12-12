package com.smartcoding.web.controller.job;

import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.vo.JobAddressGroupVO;
import com.smartcoding.schedule.core.model.vo.JobGroupVO;
import com.smartcoding.schedule.service.XxlJobGroupService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/jobGroup")
@Api(tags = "执行器管理接口")
public class JobGroupController {


    @Resource
    private XxlJobGroupService xxlJobGroupService;

    @GetMapping("/list")
    @ApiOperation("执行器列表")
    public ResultModel getExecutorList() {
        return ResultModel.success(xxlJobGroupService.findAll());
    }

    @RequestMapping("/pageList")
    @ApiOperation("执行器列表")
    @PreAuthorize("@ss.hasPermi('job:jobGroup:list')")
    public ResultModel pageList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                @RequestParam(value = "jobGroup", required = false) Long id,
                                @RequestParam(value = "addressType", required = false) Integer addressType,
                                @RequestParam(value = "alarmStatus", required = false) Integer alarmStatus) {
        PageInfo<JobGroupVO> simplePageInfo = xxlJobGroupService.pageList(pageNum, pageSize, id,addressType,alarmStatus);
        return ResultModel.success(simplePageInfo);
    }
    /**
     * 新增任务执行器
     */
    @PostMapping("/save")
    @PreAuthorize("@ss.hasPermi('job:jobGroup:add')")
    @Log(title = "新增任务执行器", businessType = BusinessType.INSERT)
    public ResultModel save(@RequestBody XxlJobGroup jobGroup) {

        int ret = xxlJobGroupService.addJobGroup(jobGroup);

        return (ret > 0) ? ResultModel.success(ret) : ResultModel.fail();
    }
    /**
     * 更新任务执行器
     */
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('job:jobGroup:update')")
    @Log(title = "更新任务执行器", businessType = BusinessType.UPDATE)
    public ResultModel update(@RequestBody XxlJobGroup jobGroupVO) {
        int ret = xxlJobGroupService.update(jobGroupVO);
        return (ret > 0) ? ResultModel.success(ret) : ResultModel.fail();
    }
    /**
     * 删除任务执行器
     */
    @PostMapping("/remove")
    @PreAuthorize("@ss.hasPermi('job:jobGroup:remove')")
    @Log(title = "删除任务执行器", businessType = BusinessType.DELETE)
    public ResultModel remove(@RequestParam("id") Long id) {
        int ret = xxlJobGroupService.remove(id);
        return (ret > 0) ? ResultModel.success(ret) : ResultModel.fail();
    }
    /**
     * 根据id获取执行器
     */
    @RequestMapping(value = "/loadById", method = RequestMethod.GET)
    @ApiOperation("根据id获取执行器")
    @PreAuthorize("@ss.hasPermi('job:jobGroup:detail')")
    public ResultModel loadById(@RequestParam("id") Long id) {
        XxlJobGroup xxlJobGroup = xxlJobGroupService.loadById(id);

        return xxlJobGroup != null ? ResultModel.success(xxlJobGroup) : ResultModel.fail();
    }
    /**
     * 根据执行器id获取在线地址注册的机器列表
     */
    @RequestMapping(value = "/getOnLineAddressList", method = RequestMethod.GET)
    @PreAuthorize("@ss.hasPermi('job:jobGroup:getOnLineAddressList')")
    public ResultModel getOnLineAddressList(@RequestParam("id") Long id) {
        JobAddressGroupVO onLineAddressList = xxlJobGroupService.getOnLineAddressList(id);
        return ResultModel.success(onLineAddressList);
    }
    /**
     * 搜索任务执行器列表
     */
    @GetMapping("/querySearch")
    public ResultModel querySearch(@RequestParam(value = "queryStr", required = false) String title, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<XxlJobGroup> list = xxlJobGroupService.querySearch(title, pageSize);
        return ResultModel.success(list);
    }
}
