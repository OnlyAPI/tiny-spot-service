package com.yifan.admin.api.annotition;

import java.lang.annotation.*;

/**
 * 校验验证码注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaptchaCheck {

    public String value() default "";

}
