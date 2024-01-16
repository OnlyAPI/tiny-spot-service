package com.yifan.admin.api.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogRequestInterceptor implements RequestInterceptor {

    @SneakyThrows
    @Override
    public void apply(RequestTemplate template) {
        log.info("[request] url: {}, queries: {}, body: {}, header: {}.",
                template.url(), template.queries(), template.body() == null ? "" : new String(template.body()), template.headers());
    }
}
