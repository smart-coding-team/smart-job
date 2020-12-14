package cn.smartcoding.web.controller.monitor;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.poi.ExcelUtil;
import cn.smartcoding.system.domain.SysLogininfor;
import cn.smartcoding.system.service.ISysLogininforService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/monitor/logininfor")
public class SysLogininforController extends BaseController {
    @Autowired
    private ISysLogininforService logininforService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "ipaddr", required = false) String ipaddr,
                            @RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "userName", required = false) String userName,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {


        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setIpaddr(ipaddr);
        logininfor.setStatus(status);
        logininfor.setUserName(userName);
        logininfor.setBeginTime(beginTime);
        logininfor.setEndTime(endTime);
        PageInfo<SysLogininfor> list = logininforService.selectLogininforList(pageNum, pageSize, logininfor);
        return ResultModel.success(list);
    }

    @Log(title = "登陆日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    public ResultModel export(SysLogininfor logininfor) {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        return util.exportExcel(list, "登陆日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登陆日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public ResultModel remove(@PathVariable Long[] infoIds) {

        return ResultModelUtils.toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登陆日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public ResultModel clean() {
        logininforService.cleanLogininfor();
        return ResultModel.success();
    }
}
