package cn.smartcoding.web.controller.system;

import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.entity.SysMenu;
import cn.smartcoding.common.core.domain.entity.SysUser;
import cn.smartcoding.common.core.domain.model.LoginUser;
import cn.smartcoding.common.utils.ServletUtils;
import cn.smartcoding.framework.web.service.TokenService;
import cn.smartcoding.system.dto.SysUserDto;
import cn.smartcoding.system.service.ISysMenuService;
import cn.smartcoding.system.service.SysLoginService;
import cn.smartcoding.system.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    ///**
    // * 登录方法
    // *
    // * @param loginBody 登录信息
    // * @return 结果
    // */
    //@PostMapping("/login")
    //public ResultModel login(@RequestBody LoginBody loginBody) {
    //    // 生成令牌
    //    LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //    String token = tokenService.createToken(loginUser);
    //    Map<String, String> tokenMap = new HashMap<>();
    //    tokenMap.put(Constants.TOKEN, token);
    //    return ResultModel.success(tokenMap);
    //}

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public ResultModel getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user.isAdmin(), user.getUserId());
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setUser(user);
        sysUserDto.setRoles(roles);
        sysUserDto.setPermissions(permissions);
        return ResultModel.success(sysUserDto);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public ResultModel getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return ResultModel.success(menuService.buildMenus(menus));
    }
}
