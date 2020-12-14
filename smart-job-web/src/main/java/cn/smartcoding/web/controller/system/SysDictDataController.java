package cn.smartcoding.web.controller.system;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.entity.SysDictData;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.common.utils.poi.ExcelUtil;
import cn.smartcoding.system.service.ISysDictDataService;
import cn.smartcoding.system.service.ISysDictTypeService;
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
@RequestMapping("/api/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "dictType", required = false) String dictType,
                            @RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "dictLabel", required = false) String dictLabel,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {

        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        dictData.setDictLabel(dictLabel);
        dictData.setStatus(status);
        dictData.setBeginTime(beginTime);
        dictData.setEndTime(endTime);
        PageInfo<SysDictData> list = dictDataService.selectDictDataList(pageNum, pageSize, dictData);
        return ResultModel.success(list);
    }


    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public ResultModel export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public ResultModel getInfo(@PathVariable Long dictCode) {
        return ResultModel.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public ResultModel dictType(@PathVariable String dictType) {
        return ResultModel.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public ResultModel remove(@PathVariable Long[] dictCodes) {
        return ResultModelUtils.toAjax(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
