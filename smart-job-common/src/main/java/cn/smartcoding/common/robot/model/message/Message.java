package cn.smartcoding.common.robot.model.message;

/**
 * 消息接口
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:40 下午
 */
public interface Message {

    /**
     * 返回消息的Json格式字符串
     *
     * @return 消息的Json格式字符串
     */
    String toJsonString();
}
