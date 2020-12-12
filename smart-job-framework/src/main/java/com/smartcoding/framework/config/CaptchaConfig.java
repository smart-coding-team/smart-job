package com.smartcoding.framework.config;

import com.smartcoding.framework.config.properties.CaptchaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author wuque
 */
@Configuration
public class CaptchaConfig {

    @Bean
    @ConfigurationProperties(prefix = "login", ignoreUnknownFields = true)
    public CaptchaProperties captchaProperties() {
        return new CaptchaProperties();
    }
}
