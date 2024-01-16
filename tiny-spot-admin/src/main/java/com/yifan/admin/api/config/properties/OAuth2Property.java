package com.yifan.admin.api.config.properties;

import com.yifan.admin.api.exception.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 11:21
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "oauth2")
@Component
public class OAuth2Property {

    private Map<String, AppConfig> apps;

    @Getter
    @Setter
    public static class AppConfig {

        private String endPoint;

        private String endPointApi;

        private String clientId;

        private String clientSecret;

        private String grantType;

        private String redirectUri;

    }

    public AppConfig getAppConfig(String name) {
        return Optional.ofNullable(this.apps.get(name))
                .orElseThrow(() -> new ApiException("配置错误"));
    }
}
