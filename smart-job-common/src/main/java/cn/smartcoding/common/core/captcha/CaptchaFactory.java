package cn.smartcoding.common.core.captcha;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.exception.CommonException;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

import java.awt.*;

public final class CaptchaFactory {

    public static Captcha switchCaptcha(CaptchaCode captchaCode) {
        Captcha captcha;
        switch (captchaCode.getCodeType()) {
            case arithmetic:
                // 算术类型 https://gitee.com/whvse/EasyCaptcha
                captcha = new ArithmeticCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                // 几位数运算，默认是两位
                captcha.setLen(captchaCode.getLength());
                break;
            case chinese:
                captcha = new ChineseCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case chinese_gif:
                captcha = new ChineseGifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case gif:
                captcha = new GifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case spec:
                captcha = new SpecCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                if (captchaCode.getLength() < 4) {
                    captchaCode.setLength(4);
                }
                captcha.setLen(captchaCode.getLength());
                break;
            default:
                throw new CommonException(CommonErrorCode.ERROR, "验证码配置信息错误！正确配置查看 CaptchaCodeEnum ");
        }
        if (StrUtil.isNotBlank(captchaCode.getFontName())) {
            captcha.setFont(new Font(captchaCode.getFontName(), Font.PLAIN, captchaCode.getFontSize()));
        }
        return captcha;
    }
}
