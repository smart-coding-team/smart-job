package com.smartcoding.web.controller.job;

import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.core.controller.BaseController;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.common.utils.SecurityUtils;
import com.smartcoding.schedule.core.model.XxlJobThread;
import com.smartcoding.schedule.service.IXxlJobTreadService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.smartcoding.common.core.domain.util.ResultModelUtils.toAjax;

/**
 * 任务调度Controller
 *
 * @author wuque
 * @date 2020-08-29
 */
@RestController
@RequestMapping("/api/jobThread")
public class JobThreadController extends BaseController {
    @Autowired
    private IXxlJobTreadService iXxlJobTreadService;

    /**
     * 查询任务调度列表
     */
    @PreAuthorize("@ss.hasPermi('job:jobThread:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "address", required = false) String address,
                            @RequestParam(value = "threadType", required = false) Integer threadType,
                            @RequestParam(value = "threadStatus", required = false) Integer threadStatus) {

        XxlJobThread dictData = new XxlJobThread();
        dictData.setAddress(address);
        dictData.setThreadType(threadType);
        dictData.setThreadStatus(threadStatus);
        PageInfo<XxlJobThread> list = iXxlJobTreadService.selectXxlJobThreadList(pageNum, pageSize, dictData);
        return ResultModel.success(list);
    }

    /**
     * 获取任务调度详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:jobThread:query')")
    @GetMapping(value = "/{id}")
    public ResultModel getInfo(@PathVariable("id") Long id) {
        return ResultModel.success(iXxlJobTreadService.selectXxlJobThreadById(id));
    }

    /**
     * 新增任务调度
     */
    @PreAuthorize("@ss.hasPermi('job:jobThread:add')")
    @Log(title = "任务调度", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@RequestBody XxlJobThread xxlJobThread) {
        xxlJobThread.setUpdateTime(new Date());
        xxlJobThread.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(iXxlJobTreadService.insertXxlJobThread(xxlJobThread));
    }

    /**
     * 修改任务调度
     */
    @PreAuthorize("@ss.hasPermi('job:jobThread:edit')")
    @Log(title = "任务调度", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@RequestBody XxlJobThread xxlJobThread) {
        return toAjax(iXxlJobTreadService.updateXxlJobThread(xxlJobThread));
    }

    /**
     * 删除任务调度
     */
    @PreAuthorize("@ss.hasPermi('job:jobThread:remove')")
    @Log(title = "删除监控线程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ResultModel remove(@PathVariable Long[] ids) {
        return toAjax(iXxlJobTreadService.deleteXxlJobThreadByIds(ids));
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @PreAuthorize("@ss.hasPermi('job:jobThread:changeStatus')")
    @Log(title = "线程状态切换", businessType = BusinessType.UPDATE)
    public ResultModel changeStatus(Long id, Integer status) {
        iXxlJobTreadService.changeStatus(id, status, SecurityUtils.getUsername());
        return ResultModel.success();
    }

}
