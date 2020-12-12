

package com.smartcoding.schedule.core.util;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * @author 无缺
 * @date 2019-07-13
 */
public class StringBuilderUtil {


    public static String listToString(List<String> itemList) {
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtil.isNotEmpty(itemList)) {
            for (String item : itemList) {
                stringBuilder.append(item).append(",");
            }
            if (stringBuilder.lastIndexOf(",") > 0) {
                stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
            }

        }
        return stringBuilder.toString();
    }
}
