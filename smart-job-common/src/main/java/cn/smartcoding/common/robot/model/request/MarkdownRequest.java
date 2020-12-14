package cn.smartcoding.common.robot.model.request;

import cn.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import cn.smartcoding.common.robot.enums.NotifyRobotEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * MARKDOWN请求,微信和钉钉使用有差异
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:40 下午
 */
@Data
@Accessors(chain = true)
public class MarkdownRequest extends BaseNotifyRequest implements RobotNotifyRequest {
    //wx只有这个字段
    //private List<String> items = new ArrayList<String>();

    /**
     * DingDing具有的字段
     */
    private String title;

    private List<String> items = new ArrayList<String>();
    /**
     * 被@人的手机号(在text内容里要有@手机号)
     */
    private List<String> atMobileList = new ArrayList<>();
    /**
     * (@所有人时：true，否则为：false)
     */
    private boolean isAtAll = false;

    public MarkdownRequest addItem(String item) {
        this.items.add(item);
        return this;
    }

    public MarkdownRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.MARKDOWN;
    }
}
