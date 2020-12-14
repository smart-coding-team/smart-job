

package cn.smartcoding.schedule.core.util;

import org.springframework.util.StringUtils;

/**
 * @author 无缺
 * @date 2019-11-24
 */
public class VO2DOUtils {
    public static String hideMobile(String mobile) {
        if (StringUtils.isEmpty(mobile) || mobile.length() < 11) {
            return mobile;
        }
        return mobile.substring(0,3) + "****" + mobile.substring(7);
    }
}
