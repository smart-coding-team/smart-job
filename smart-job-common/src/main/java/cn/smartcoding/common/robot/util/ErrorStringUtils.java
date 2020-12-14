package cn.smartcoding.common.robot.util;

/**
 * --添加相关注释--
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 12:35 下午
 */
public class ErrorStringUtils {
    public static String wxError(String msg) {
        return "企业微信: " + msg;
    }

    public static String dingdingError(String msg) {
        return "钉钉: " + msg;
    }
}
