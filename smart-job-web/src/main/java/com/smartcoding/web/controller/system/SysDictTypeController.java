package com.smartcoding.web.controller.system;

import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.constant.UserConstants;
import com.smartcoding.common.core.controller.BaseController;
import com.smartcoding.common.core.domain.CommonErrorCode;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.core.domain.entity.SysDictType;
import com.smartcoding.common.core.domain.util.ResultModelUtils;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.common.utils.SecurityUtils;
import com.smartcoding.common.utils.poi.ExcelUtil;
import com.smartcoding.system.service.ISysDictTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "dictType", required = false) String dictType,
                            @RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "dictName", required = false) String dictName,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {

        SysDictType sysDictType = new SysDictType();
        sysDictType.setDictType(dictType);
        sysDictType.setDictName(dictName);
        sysDictType.setStatus(status);
        sysDictType.setBeginTime(beginTime);
        sysDictType.setEndTime(endTime);
        PageInfo<SysDictType> list = dictTypeService.selectDictTypeList(pageNum, pageSize, sysDictType);
        return ResultModel.success(list);
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public ResultModel export(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public ResultModel getInfo(@PathVariable Long dictId) {
        return ResultModel.success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public ResultModel remove(@PathVariable Long[] dictIds) {
        return ResultModelUtils.toAjax(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public ResultModel clearCache() {
        dictTypeService.clearCache();
        return ResultModel.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public ResultModel optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return ResultModel.success(dictTypes);
    }
}
