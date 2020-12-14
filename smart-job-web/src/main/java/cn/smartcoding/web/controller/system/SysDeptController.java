package cn.smartcoding.web.controller.system;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.constant.UserConstants;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.entity.SysDept;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.common.utils.StringUtils;
import cn.smartcoding.system.dto.SysDeptDto;
import cn.smartcoding.system.service.ISysDeptService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public ResultModel list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return ResultModel.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public ResultModel excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return ResultModel.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public ResultModel getInfo(@PathVariable Long deptId) {
        return ResultModel.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public ResultModel treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return ResultModel.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public ResultModel roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        SysDeptDto sysDeptDto = new SysDeptDto();
        sysDeptDto.setCheckedKeys(deptService.selectDeptListByRoleId(roleId));
        sysDeptDto.setDepts(deptService.buildDeptTreeSelect(depts));
        return ResultModel.success(sysDeptDto);
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return ResultModel.fail(CommonErrorCode.ERROR, "修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return ResultModel.fail(CommonErrorCode.ERROR, "修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return ResultModel.fail(CommonErrorCode.ERROR, "该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public ResultModel remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return ResultModel.fail(CommonErrorCode.ERROR, "存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return ResultModel.fail(CommonErrorCode.ERROR, "部门存在用户,不允许删除");
        }
        return ResultModelUtils.toAjax(deptService.deleteDeptById(deptId));
    }
}
