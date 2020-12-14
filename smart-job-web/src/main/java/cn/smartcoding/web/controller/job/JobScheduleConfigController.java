package cn.smartcoding.web.controller.job;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.poi.ExcelUtil;
import cn.smartcoding.schedule.core.model.XxlJobScheduleConfig;
import cn.smartcoding.schedule.service.IXxlJobScheduleConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.smartcoding.common.core.domain.util.ResultModelUtils.toAjax;

/**
 * 任务管理配置Controller
 *
 * @author wuque
 * @date 2020-08-29
 */
@RestController
@RequestMapping("/api/config")
public class JobScheduleConfigController extends BaseController {
    @Autowired
    private IXxlJobScheduleConfigService xxlJobScheduleConfigService;

    /**
     * 查询任务管理配置列表
     */
    @PreAuthorize("@ss.hasPermi('job:config:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "configKey", required = false) String configKey,
                            @RequestParam(value = "configStatus", required = false) Integer configStatus) {

        XxlJobScheduleConfig dictData = new XxlJobScheduleConfig();
        dictData.setConfigKey(configKey);
        dictData.setConfigStatus(configStatus);
        PageInfo<XxlJobScheduleConfig> list = xxlJobScheduleConfigService.selectXxlJobScheduleConfigList(pageNum, pageSize, dictData);
        return ResultModel.success(list);
    }

    /**
     * 导出任务管理配置列表
     */
    @PreAuthorize("@ss.hasPermi('job:config:export')")
    @Log(title = "任务管理配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResultModel export(XxlJobScheduleConfig xxlJobScheduleConfig) {
        List<XxlJobScheduleConfig> list = xxlJobScheduleConfigService.selectXxlJobScheduleConfigList(xxlJobScheduleConfig);
        ExcelUtil<XxlJobScheduleConfig> util = new ExcelUtil<XxlJobScheduleConfig>(XxlJobScheduleConfig.class);
        return util.exportExcel(list, "config");
    }

    /**
     * 获取任务管理配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:config:query')")
    @GetMapping(value = "/{id}")
    public ResultModel getInfo(@PathVariable("id") Long id) {
        return ResultModel.success(xxlJobScheduleConfigService.selectXxlJobScheduleConfigById(id));
    }

    /**
     * 新增任务管理配置
     */
    @PreAuthorize("@ss.hasPermi('job:config:add')")
    @Log(title = "任务管理配置", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@RequestBody XxlJobScheduleConfig xxlJobScheduleConfig) {
        return toAjax(xxlJobScheduleConfigService.insertXxlJobScheduleConfig(xxlJobScheduleConfig));
    }

    /**
     * 修改任务管理配置
     */
    @PreAuthorize("@ss.hasPermi('job:config:edit')")
    @Log(title = "任务管理配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@RequestBody XxlJobScheduleConfig xxlJobScheduleConfig) {
        return toAjax(xxlJobScheduleConfigService.updateXxlJobScheduleConfig(xxlJobScheduleConfig));
    }

    /**
     * 删除任务管理配置
     */
    @PreAuthorize("@ss.hasPermi('job:config:remove')")
    @Log(title = "任务管理配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ResultModel remove(@PathVariable Long[] ids) {
        return toAjax(xxlJobScheduleConfigService.deleteXxlJobScheduleConfigByIds(ids));
    }
}
