package com.smartcoding.common.robot.notify;

import com.alibaba.fastjson.JSONObject;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.model.request.RobotNotifyRequest;
import com.smartcoding.common.robot.model.response.RobotNotifyResponse;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 7:42 下午
 */
public abstract class AbstractRobotNotify implements RobotNotify {

    /**
     * 拼接发送通知消息
     *
     * @param robotNotifyRequest 通知消息
     * @return 消息
     */
    public abstract Message buildMessage(RobotNotifyRequest robotNotifyRequest);

    /**
     * 发送通知消息
     *
     * @param robotNotifyRequest 通知消息
     * @return 结果
     */
    public abstract RobotNotifyResponse sendRobotNotify(RobotNotifyRequest robotNotifyRequest);

    /**
     * 获取通知机器人类型
     *
     * @return 机器人类型
     */
    public abstract NotifyRobotEnum getNotifyRobotEnum();

    @Override
    public RobotNotifyResponse sendRobotSyncNotify(RobotNotifyRequest robotNotifyRequest) {
        return sendRobotNotify(robotNotifyRequest);
    }

    @Override
    public RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest robotNotifyRequest) {
        try {
            FutureTask<RobotNotifyResponse> futureTask = new FutureTask<>(() -> sendRobotNotify(robotNotifyRequest));
            CompletableFuture.runAsync(futureTask);
            return futureTask.get();
        } catch (Exception e) {
            return RobotNotifyResponse.error(e.getMessage());
        }
    }

    @Override
    public RobotNotifyResponse sendRobotAsyncNotify(RobotNotifyRequest robotNotifyRequest, Executor executor) {
        try {
            FutureTask<RobotNotifyResponse> futureTask = new FutureTask<>(() -> sendRobotNotify(robotNotifyRequest));
            CompletableFuture.runAsync(futureTask, executor);
            return futureTask.get();
        } catch (Exception e) {
            return RobotNotifyResponse.error(e.getMessage());
        }
    }

    protected RobotNotifyResponse buildRobotNotifyResponse(HttpResponse response) throws IOException {
        RobotNotifyResponse robotNotifyResponse = new RobotNotifyResponse();
        String result = EntityUtils.toString(response.getEntity());
        JSONObject obj = JSONObject.parseObject(result);

        Integer errcode = obj.getInteger("errcode");
        robotNotifyResponse.setErrorCode(errcode);
        robotNotifyResponse.setErrorMsg(obj.getString("errmsg"));
        robotNotifyResponse.setSuccess(errcode.equals(0));
        return robotNotifyResponse;
    }

}
