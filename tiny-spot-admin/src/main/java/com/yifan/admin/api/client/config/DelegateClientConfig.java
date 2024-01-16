package com.yifan.admin.api.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yifan.admin.api.client.oAuth.GiteeClient;
import com.yifan.admin.api.client.oAuth.GithubApiClient;
import com.yifan.admin.api.client.oAuth.GithubClient;
import com.yifan.admin.api.client.oAuth.QQApiClient;
import com.yifan.admin.api.config.properties.OAuth2Property;
import feign.Client;
import feign.Feign;
import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * 客户端注册
 */
@Slf4j
@Configuration
public class DelegateClientConfig {

    @Resource
    private OAuth2Property oAuth2Property;

    @Bean(name = "giteeClient")
    public GiteeClient giteeClient() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Feign.builder()
                .logLevel(Logger.Level.NONE)
                .encoder(new GetQueryAndPostJacksonEncoder(objectMapper))
                .requestInterceptor(new LogRequestInterceptor())
                .decoder(new LogJacksonDecoder(objectMapper))
                .target(GiteeClient.class, oAuth2Property.getApps().get("gitee").getEndPoint());
    }

    @Bean(name = "qqClient")
    public QQApiClient qqApiClient() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Feign.builder()
                .logLevel(Logger.Level.NONE)
                .encoder(new GetQueryAndPostJacksonEncoder(objectMapper))
                .requestInterceptor(new LogRequestInterceptor())
                .decoder(new LogJacksonDecoder(objectMapper))
                .target(QQApiClient.class, oAuth2Property.getApps().get("qq").getEndPoint());
    }

    @Bean(name = "githubClient")
    public GithubClient githubClient() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Feign.builder()
                .logLevel(Logger.Level.NONE)
                .encoder(new GetQueryAndPostJacksonEncoder(objectMapper))
                .requestInterceptor(new LogRequestInterceptor())
                .decoder(new LogJacksonDecoder(objectMapper))
                .client(aDefault())
                .target(GithubClient.class, oAuth2Property.getApps().get("github").getEndPoint());
    }

    @Bean(name = "githubApiClient")
    public GithubApiClient githubApiClient() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Feign.builder()
                .logLevel(Logger.Level.NONE)
                .encoder(new GetQueryAndPostJacksonEncoder(objectMapper))
                .requestInterceptor(new LogRequestInterceptor())
                .decoder(new LogJacksonDecoder(objectMapper))
                .client(aDefault())
                .target(GithubApiClient.class, oAuth2Property.getApps().get("github").getEndPointApi());
    }


    @Bean
    public Client.Default aDefault() {
        return new Client.Default(getSSLSocketFactory(), noopHostnameVerifier());
    }

    @Bean
    public SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Bean
    public NoopHostnameVerifier noopHostnameVerifier() {
        return new NoopHostnameVerifier();
    }

}
