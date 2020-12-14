package cn.smartcoding.web.controller.system;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.system.domain.SysLdapConfig;
import cn.smartcoding.system.service.ISysLdapConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统设置
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/setting")
public class SysSettingController extends BaseController {

    @Autowired
    private ISysLdapConfigService sysLdapConfigService;


    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ldapConfig:info')")
    @GetMapping(value = "/ldap/info")
    public ResultModel getInfo() {
        return ResultModel.success(sysLdapConfigService.getSysDapConfig());
    }

    /**
     * 修改LDAP参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:ldapConfig:edit')")
    @Log(title = "LDAP管理", businessType = BusinessType.UPDATE)
    @PostMapping("/ldap/update")
    public ResultModel edit(@Validated @RequestBody SysLdapConfig config) {
        config.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(sysLdapConfigService.update(config));
    }

    /**
     * 修改LDAP参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:ldapConfig:edit')")
    @Log(title = "LDAP管理", businessType = BusinessType.INSERT)
    @PostMapping("/ldap/add")
    public ResultModel add(@Validated @RequestBody SysLdapConfig config) {
        config.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(sysLdapConfigService.addLdapConfig(config));
    }

    /**
     * test测试
     */
    @PreAuthorize("@ss.hasPermi('system:ldapConfig:test')")
    @Log(title = "LDAP管理", businessType = BusinessType.QUERY)
    @PostMapping("/ldap/test")
    public ResultModel testLdap(@Validated @RequestBody SysLdapConfig config) {
        sysLdapConfigService.testLdap(config);
        return ResultModel.success();
    }

}
