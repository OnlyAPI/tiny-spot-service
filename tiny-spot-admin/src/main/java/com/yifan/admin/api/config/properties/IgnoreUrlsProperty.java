package com.yifan.admin.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于配置白名单资源路径
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
@Component
public class IgnoreUrlsProperty {

    private List<String> urls = new ArrayList<>();

}
