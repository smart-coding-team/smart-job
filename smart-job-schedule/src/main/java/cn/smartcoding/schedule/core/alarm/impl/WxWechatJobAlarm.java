package cn.smartcoding.schedule.core.alarm.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.core.domain.entity.SysUser;
import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import cn.smartcoding.common.robot.model.request.TextRequest;
import cn.smartcoding.common.robot.model.response.RobotNotifyRequestBuilder;
import cn.smartcoding.common.robot.model.response.RobotNotifyResponse;
import cn.smartcoding.common.robot.notify.RobotNotify;
import cn.smartcoding.schedule.core.alarm.*;
import cn.smartcoding.schedule.core.exception.AlarmFailException;
import cn.smartcoding.schedule.core.model.XxlAlarmInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(2)
public class WxWechatJobAlarm extends AbstractJobAlarm {


    @Override
    public AlarmResult sendAlarm(XxlAlarmInfo alarmInfo, List<SysUser> noticeUserList, String sendContent) {
        String alarmUrl = alarmInfo.getAlarmUrl();
        if (StrUtil.isEmpty(alarmUrl)) {
            return new DefaultFailAlarmResult("企业微信告警地址为空，发送失败");
        }
        try {
            List<String> mobileList = getMobileList(noticeUserList);
            boolean atAll = CollectionUtil.isEmpty(mobileList);
            TextRequest textRequest = RobotNotifyRequestBuilder
                    .textMessageRequest(alarmUrl, NotifyRobotEnum.CORP_WECHAT)
                    .setText(sendContent)
                    .setAtAll(atAll)
                    .setAtMobileList(mobileList);
            RobotNotifyResponse notifyResponse = RobotNotify.defaultRobotNotify().sendRobotSyncNotify(textRequest);
            return new DefaultAlarmResult(notifyResponse.isSuccess(), notifyResponse.getErrorMsg());
        } catch (Exception e) {
            throw new AlarmFailException("企业微信通知发送失败", e);
        }
    }

    @Override
    public boolean support(JobAlarmEnum jobAlarmEnum) {
        return JobAlarmEnum.WX_WECHAT.equals(jobAlarmEnum);
    }
}
