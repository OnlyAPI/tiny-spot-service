package com.yifan.admin.api.aspect;

import com.yifan.admin.api.annotition.CaptchaCheck;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.params.CommonLoginParam;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码校验-切面
 */
@Aspect
@Component
public class CaptchaCheckAspect {

    private static final Logger log = LoggerFactory.getLogger(CaptchaCheckAspect.class);

    @Autowired
    CacheService cacheService;

    //配置织入点
    @Pointcut(value = "@annotation(com.yifan.admin.api.annotition.CaptchaCheck)")
    public void access() {

    }

    /*前置增强*/
    @Before("access() && @annotation(captchaCheck)")
    public void doBefore(JoinPoint joinPoint, CaptchaCheck captchaCheck) {
        log.info("[CaptchaCHeckAspect - doBefore] doBefore-type: {}", captchaCheck.value());

        Object[] arg = joinPoint.getArgs();

        String paramStr = JacksonJsonUtil.toJson(arg[0]);
        CommonLoginParam params = JacksonJsonUtil.fromJson(paramStr, CommonLoginParam.class);

        final String captcha = params.getCode();
        final String randomStr = params.getUuid();

        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(randomStr)) {
            log.error("[CaptchaCHeckAspect - doBefore] captcha is empty. username: {}.", params.getUsername());
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(), "验证码/uuid 不能为空");
        }

        final String cache_code = cacheService.getCapacha(randomStr);
        if (StringUtils.isEmpty(cache_code)) {
            log.error("[CaptchaCHeckAspect - doBefore] captcha timeout. username: {}.", params.getUsername());
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(), "验证码已过期");
        }

        if (!captcha.equalsIgnoreCase(cache_code)) {
            log.error("[CaptchaCHeckAspect - doBefore] captcha error. username: {}.", params.getUsername());
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(), "验证码错误");
        }

        Boolean delOldCodeResult = cacheService.deleteCapacha(randomStr);
        log.info("[CaptchaCHeckAspect - doBefore] delete old code result: {}.", delOldCodeResult);

    }
}
