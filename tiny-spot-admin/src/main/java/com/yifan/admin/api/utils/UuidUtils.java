package com.yifan.admin.api.utils;

import java.util.UUID;

/**
 * ID生成器工具类
 *
 */
public class UuidUtils {
    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
