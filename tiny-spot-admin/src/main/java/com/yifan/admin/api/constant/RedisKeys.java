package com.yifan.admin.api.constant;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 15:01
 */
public class RedisKeys {

    private static final String BASE_FORMAT_KEY = "%s::%s::%s";
    private static final String PREFIX = "f-springboot";

    public static String baiduAccessTokenKey() {
        return String.format(BASE_FORMAT_KEY, PREFIX, "baidu", "accessToken");
    }

    public static String tokenKey(String token) {
        return String.format(BASE_FORMAT_KEY, PREFIX, "token", token);
    }

    public static String captchaKey(String uuid) {
        return String.format(BASE_FORMAT_KEY, PREFIX, "captcha", uuid);
    }

    public static String permissionKey(Long userId) {
        return String.format(BASE_FORMAT_KEY, PREFIX, "permission", userId.toString());
    }

    public static String menusKey(Long userId) {
        return String.format(BASE_FORMAT_KEY, PREFIX, "menu", userId.toString());
    }

    public static String wsTokenKey(String wsToken) {
        return String.format(BASE_FORMAT_KEY, PREFIX, "ws-token", wsToken);
    }

}
