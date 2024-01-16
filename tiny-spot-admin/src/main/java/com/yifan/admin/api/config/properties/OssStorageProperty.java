package com.yifan.admin.api.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio存储服务
 * @author TaiYi
 * @ClassName
 * @date 2023/3/31 17:34
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "oss")
@Component
public class OssStorageProperty {

    private String type;

    private Local local = new Local();

    private Minio minio = new Minio();


    @Data
    public static class Local {
        private String path;
        private String prefix;
    }

    @Data
    public static class Minio {
        private String accessKey;

        private String secretKey;

        private String bucket;

        private String endPoint;
    }

}
