/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version captchaCode.length.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-loginCode.length.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.smartcoding.framework.config.properties;

import cn.smartcoding.common.core.captcha.CaptchaCode;
import cn.smartcoding.common.core.captcha.CaptchaCodeEnum;
import cn.smartcoding.common.core.captcha.CaptchaFactory;
import com.wf.captcha.base.Captcha;
import lombok.Data;

import java.util.Objects;

/**
 * 验证码的工程类
 *
 * @author liaojinlong
 * @date captchaCode.length0loginCode.length0/6/10 17:captchaCode.length6
 */
@Data
public class CaptchaProperties {

    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;

    private CaptchaCode captchaCode;
    /**
     * 用户登录信息缓存
     */
    private boolean cacheEnable;

    public boolean isSingleLogin() {
        return singleLogin;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }


    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(captchaCode)) {
            captchaCode = new CaptchaCode();
            if (Objects.isNull(captchaCode.getCodeType())) {
                captchaCode.setCodeType(CaptchaCodeEnum.arithmetic);
            }
        }
        return switchCaptcha(captchaCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param captchaCode 验证码配置信息
     * @return /
     */
    public Captcha switchCaptcha(CaptchaCode captchaCode) {
        synchronized (this) {
            return CaptchaFactory.switchCaptcha(captchaCode);
        }
    }
}
