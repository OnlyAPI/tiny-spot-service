package com.yifan.admin.api.context;

import com.yifan.admin.api.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

public final class RequestContext {

    private RequestContext() {

    }

    private static final ThreadLocal<RequestContent> REQUEST_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();


    public static void set(String token, SysUser user) {
        RequestContent content = new RequestContent(token, user);
        REQUEST_CONTEXT_THREAD_LOCAL.set(content);
    }

    public static void clear() {
        REQUEST_CONTEXT_THREAD_LOCAL.remove();
    }

    public static String getToken() {
        return getValue(RequestContent::getToken);
    }

    public static SysUser getUser() {
        return getValue(RequestContent::getUser);
    }

    private static <T> T getValue(Function<RequestContent, T> func) {
        RequestContent content = REQUEST_CONTEXT_THREAD_LOCAL.get();
        if (content == null) {
            return null;
        }

        return func.apply(content);
    }

    @Data
    @AllArgsConstructor
    static class RequestContent {
        private final String token;
        private final SysUser user;
    }
}
