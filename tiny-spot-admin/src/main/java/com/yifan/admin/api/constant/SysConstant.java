package com.yifan.admin.api.constant;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/4/10 16:13
 */
public class SysConstant {

    //token-key
    public static final String HEAD_TOKEN_KEY = "token";

    //缓存中token有效期-s
    public static final Long CACHE_TOKEN_TIME_OUT = 300000L;

    //缓存中验证码有效期-s
    public static final Long CACHE_CAPTCHA_TIME_OUT = 60L;

    //缓存-超时时间-s
    public static final Long CACHE_TIME_OUT = 1800L;

    public static final String INIT_PASSWORD = "123456";

    public static final String WS_USER_ID = "UID";
}
