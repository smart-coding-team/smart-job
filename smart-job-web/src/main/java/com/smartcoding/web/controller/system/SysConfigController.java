package com.smartcoding.web.controller.system;

import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.annotation.RepeatSubmit;
import com.smartcoding.common.constant.UserConstants;
import com.smartcoding.common.core.controller.BaseController;
import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.core.domain.util.ResultModelUtils;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.common.utils.SecurityUtils;
import com.smartcoding.common.utils.poi.ExcelUtil;
import com.smartcoding.system.domain.SysConfig;
import com.smartcoding.system.service.ISysConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "configName", required = false) String configName,
                            @RequestParam(value = "configType", required = false) String configType,
                            @RequestParam(value = "configKey", required = false) String configKey,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {

        SysConfig config = new SysConfig();
        config.setConfigName(configName);
        config.setConfigType(configType);
        config.setConfigKey(configKey);
        config.setBeginTime(beginTime);
        config.setEndTime(endTime);
        PageInfo<SysConfig> list = configService.selectConfigList(pageNum, pageSize, config);
        return ResultModel.success(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    public ResultModel export(SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public ResultModel getInfo(@PathVariable Long configId) {
        return ResultModel.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public ResultModel getConfigKey(@PathVariable String configKey) {
        return ResultModel.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public ResultModel add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public ResultModel remove(@PathVariable Long[] configIds) {
        return ResultModelUtils.toAjax(configService.deleteConfigByIds(configIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public ResultModel clearCache() {
        configService.clearCache();
        return ResultModel.success();
    }
}
