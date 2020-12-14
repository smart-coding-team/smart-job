package cn.smartcoding.job.core.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author wuque
 * @date 04/05/2017
 */
public class UUIDUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成纯数字UUID
     *
     * @return 8位数UUID
     */
    public static String generateUUID(int num) {
        String uid = generateUUID();
        return uid.substring(0, num);
    }

    /**
     * 生成8位纯数字UUID
     *
     * @return 8位数UUID
     */
    public static String generateEightUUID() {
        long uid = UUID.randomUUID().getMostSignificantBits();
        if (uid < 0) {
            uid = -uid;
        }
        String str = "" + uid;
        str = str.substring(0, 8);
        return str;
    }

    /**
     * 生成4位纯数字UUID
     *
     * @return 8位数UUID
     */
    public static String generateFourUUID() {
        long uid = UUID.randomUUID().getMostSignificantBits();
        if (uid < 0) {
            uid = -uid;
        }
        String str = "" + uid;
        str = str.substring(0, 4);
        return str;
    }
}
