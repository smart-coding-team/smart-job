package cn.smartcoding.common.robot.model.response;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求返回信息
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:18 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotNotifyResponse {
    private boolean isSuccess = false;
    private Integer errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("errorCode", errorCode);
        items.put("errorMsg", errorMsg);
        items.put("isSuccess", isSuccess);
        return JSON.toJSONString(items);
    }

    public static RobotNotifyResponse error(String errorMsg) {
        RobotNotifyResponse robotNotifyResponse = new RobotNotifyResponse();
        robotNotifyResponse.setErrorMsg(errorMsg);
        return robotNotifyResponse;
    }
}
