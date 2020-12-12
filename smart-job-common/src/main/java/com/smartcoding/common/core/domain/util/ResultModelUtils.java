package com.smartcoding.common.core.domain.util;

import com.smartcoding.common.core.domain.ResultModel;

public class ResultModelUtils {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    public static ResultModel toAjax(int rows) {
        return rows > 0 ? ResultModel.success() : ResultModel.fail();
    }
}
