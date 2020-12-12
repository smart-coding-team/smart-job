package com.smartcoding.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartcoding.common.annotation.Log;
import com.smartcoding.common.constant.UserConstants;
import com.smartcoding.common.core.controller.BaseController;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.common.core.domain.entity.SysRole;
import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.common.core.domain.model.LoginUser;
import com.smartcoding.common.core.domain.util.ResultModelUtils;
import com.smartcoding.common.enums.BusinessType;
import com.smartcoding.common.utils.SecurityUtils;
import com.smartcoding.common.utils.ServletUtils;
import com.smartcoding.common.utils.StringUtils;
import com.smartcoding.common.utils.poi.ExcelUtil;
import com.smartcoding.framework.web.service.TokenService;
import com.smartcoding.system.domain.vo.SysUserVO;
import com.smartcoding.system.service.ISysPostService;
import com.smartcoding.system.service.ISysRoleService;
import com.smartcoding.system.service.ISysUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "userName", required = false) String userName,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "deptId", required = false) Long deptId,
                            @RequestParam(value = "beginTime", required = false) String beginTime,
                            @RequestParam(value = "endTime", required = false) String endTime) {

        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setMobile(mobile);
        user.setStatus(status);
        user.setBeginTime(beginTime);
        user.setEndTime(endTime);
        user.setDeptId(deptId);
        PageInfo<SysUser> list = userService.selectUserList(pageNum, pageSize, user);
        List<SysUser> sysUserList = list.getList();
        if (CollectionUtil.isNotEmpty(sysUserList)) {
            sysUserList.forEach(sysUser -> {
                sysUser.setMobile(SecurityUtils.hidePhone(sysUser.getMobile()));
            });
        }
        return ResultModel.success(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public ResultModel export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public ResultModel importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return ResultModel.success(message);
    }

    @GetMapping("/importTemplate")
    public ResultModel importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public ResultModel getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        data.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        data.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            data.put("user", userService.selectUserById(userId));
            data.put("postIds", postService.selectPostListByUserId(userId));
            data.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ResultModel.success(data);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return ResultModel.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StrUtil.isNotEmpty(user.getMobile()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getUserId(), user.getMobile()))) {
            return ResultModel.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StrUtil.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getEmail(), user.getUserId()))) {
            return ResultModel.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return ResultModelUtils.toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StrUtil.isNotEmpty(user.getMobile()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getUserId(), user.getMobile()))) {
            return ResultModel.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StrUtil.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getEmail(), user.getUserId()))) {
            return ResultModel.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public ResultModel remove(@PathVariable Long[] userIds) {
        return ResultModelUtils.toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public ResultModel resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public ResultModel changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(userService.updateUserStatus(user));
    }

    /**
     * 查询用户
     */
    @GetMapping("/querySearch")
    public ResultModel querySearch(@RequestParam(value = "queryStr", required = false) String searchContent, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<SysUserVO> list = userService.getShortUserBO(searchContent, pageSize);
        return ResultModel.success(list);
    }

    @GetMapping("/queryIds")
    public ResultModel queryIds(@RequestParam(value = "userIds") String userIds) {
        List<Long> idList = Arrays.stream(StrUtil.splitToLong(userIds, ',')).boxed().collect(Collectors.toList());
        List<SysUser> list = userService.selectUserByIdList(idList);
        return ResultModel.success(list);
    }
}
