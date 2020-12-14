package cn.smartcoding.web.controller.job;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.schedule.core.model.XxlJobAlarmLog;
import cn.smartcoding.schedule.service.IXxlJobAlarmLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 任务告警记录Controller
 *
 * @author wuque
 * @date 2020-08-29
 */
@RestController
@RequestMapping("/api/alarmLog")
public class JobAlarmLogController extends BaseController {
    @Autowired
    private IXxlJobAlarmLogService xxlJobAlarmLogService;

    /**
     * 查询任务告警记录列表
     */
    @PreAuthorize("@ss.hasPermi('job:alarmLog:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "jobName", required = false) String jobName,
                            @RequestParam(value = "sendStatus", required = false) Boolean sendStatus,
                            @RequestParam(value = "logId", required = false) Long logId) {
        PageInfo<XxlJobAlarmLog> list = xxlJobAlarmLogService.selectXxlJobAlarmLogListPage(pageNum, pageSize, jobName, sendStatus, logId);
        return ResultModel.success(list);
    }

    /**
     * 获取任务告警记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:alarmLog:query')")
    @GetMapping(value = "/{id}")
    public ResultModel getInfo(@PathVariable("id") Long id) {
        return ResultModel.success(xxlJobAlarmLogService.selectXxlJobAlarmLogById(id));
    }


    /**
     * 删除任务告警记录
     */
    @PreAuthorize("@ss.hasPermi('job:alarmLog:remove')")
    @Log(title = "任务告警记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ResultModel remove(@PathVariable Long[] ids) {
        return ResultModelUtils.toAjax(xxlJobAlarmLogService.deleteXxlJobAlarmLogByIds(ids));
    }
}
