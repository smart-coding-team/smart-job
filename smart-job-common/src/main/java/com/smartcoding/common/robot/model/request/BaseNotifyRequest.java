package com.smartcoding.common.robot.model.request;

import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import lombok.Data;

/**
 * 请求的详情请看:
 * 钉钉: https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
 * 微信: https://work.weixin.qq.com/help?person_id=1&doc_id=13376#markdown%E7%B1%BB%E5%9E%8B
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/06- 8:17 下午
 */
@Data
public class BaseNotifyRequest {
    private String webHook;

    private NotifyRobotEnum notifyRobotEnum;


    protected BaseNotifyRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        this.webHook = webHook;
        this.notifyRobotEnum = notifyRobotEnum;
    }
}
