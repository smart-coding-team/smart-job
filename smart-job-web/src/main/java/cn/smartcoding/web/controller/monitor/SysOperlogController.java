package cn.smartcoding.web.controller.monitor;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.poi.ExcelUtil;
import cn.smartcoding.system.domain.SysOperLog;
import cn.smartcoding.system.service.ISysOperLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/monitor/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "businessType", required = false) Integer businessType,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "operName", required = false) String operName,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {

        SysOperLog operLog = new SysOperLog();
        operLog.setTitle(title);
        operLog.setBusinessType(businessType);
        operLog.setStatus(status);
        operLog.setOperName(operName);
        operLog.setBeginTime(beginTime);
        operLog.setEndTime(endTime);
        PageInfo<SysOperLog> list = operLogService.selectOperLogList(pageNum, pageSize,operLog);
        return ResultModel.success(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @GetMapping("/export")
    public ResultModel export(SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public ResultModel remove(@PathVariable Long[] operIds) {
        return ResultModelUtils.toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public ResultModel clean() {
        operLogService.cleanOperLog();
        return ResultModel.success();
    }
}
