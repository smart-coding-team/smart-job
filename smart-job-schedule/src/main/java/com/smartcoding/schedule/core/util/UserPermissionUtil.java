

package com.smartcoding.schedule.core.util;

import org.springframework.util.StringUtils;

/**
 * @author 无缺
 * @date 2019-07-09
 */
public class UserPermissionUtil {
    // plugin
    public static boolean validPermission(Integer role, String permission, Long jobGroup) {
        if (role == 1) {
            return true;
        }
        if (StringUtils.hasText(permission)) {
            for (String permissionItem : permission.split(",")) {
                if (String.valueOf(jobGroup).equals(permissionItem)) {
                    return true;
                }
            }
        }
        return false;
    }
}
