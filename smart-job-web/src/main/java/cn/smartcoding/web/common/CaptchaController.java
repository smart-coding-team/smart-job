package cn.smartcoding.web.common;

import cn.hutool.core.util.IdUtil;
import cn.smartcoding.common.constant.Constants;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.redis.RedisCache;
import cn.smartcoding.framework.config.properties.CaptchaProperties;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/")
public class CaptchaController {

    @Autowired
    private RedisCache redisCache;

    @Resource
    private CaptchaProperties captchaProperties;

    @ApiOperation("生成验证码")
    @GetMapping(value = "/code")
    public ResultModel getCode() {
        // 获取运算的结果
        Captcha captcha = captchaProperties.getCaptcha();
        String uuid = IdUtil.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        // 保存
        redisCache.setCacheObject(verifyKey, captcha.text(), Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResultModel.success(imgResult);
    }
}
