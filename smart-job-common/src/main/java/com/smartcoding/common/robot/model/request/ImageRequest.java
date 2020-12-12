package com.smartcoding.common.robot.model.request;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import com.smartcoding.common.robot.enums.NotifyMsgTypeEnum;
import com.smartcoding.common.robot.enums.NotifyRobotEnum;
import com.smartcoding.common.robot.util.HttpUtils;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 图片消息
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 9:37 下午
 */
@Data
@Accessors(chain = true)
public class ImageRequest extends BaseNotifyRequest implements RobotNotifyRequest {
    /**
     * 图片内容（base64编码前）的md5值
     */
    private String md5;
    /**
     * 图片内容的base64编码
     */
    private String base64;

    public ImageRequest(String webHook, NotifyRobotEnum notifyRobotEnum) {
        super(webHook, notifyRobotEnum);
    }

    @Override
    public NotifyMsgTypeEnum getMsgType() {
        return NotifyMsgTypeEnum.IMAGE;
    }

    /**
     * 直接从url转换为请求
     *
     * @param url
     * @return
     */
    public ImageRequest fromUrl(String url) {
        byte[] bytes = HttpUtils.getBytes(url);
        this.setMd5(SecureUtil.md5().digestHex(bytes));
        this.setBase64(Base64.encode(bytes));
        return this;
    }

    /**
     * 直接从bytes转换为请求
     *
     * @param bytes
     * @return
     */
    public ImageRequest fromBytes(byte[] bytes) {
        this.setMd5(SecureUtil.md5().digestHex(bytes));
        this.setBase64(Base64.encode(bytes));
        return this;
    }
}
