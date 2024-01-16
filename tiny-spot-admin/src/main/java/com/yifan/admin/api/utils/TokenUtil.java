package com.yifan.admin.api.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 18:27
 */
public final class TokenUtil {

    private TokenUtil() {
    }

    public static String token(Object... objs) {
        return DigestUtils.sha256Hex(Stream.of(objs).map(String::valueOf).collect(Collectors.joining()) + DateTimeUtil.currentMilli());
    }

}
