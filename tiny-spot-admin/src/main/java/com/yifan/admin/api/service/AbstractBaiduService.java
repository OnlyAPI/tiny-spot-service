package com.yifan.admin.api.service;

import com.yifan.admin.api.constant.RedisKeys;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.baidu.AccessTokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 17:33
 */
public abstract class AbstractBaiduService {

    protected final RedissonClient redissonClient;

    protected final RestTemplate restTemplate;

    public AbstractBaiduService(RedissonClient redissonClient, RestTemplate restTemplate) {
        this.redissonClient = redissonClient;
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(AiConfig aiConfig){
        RBucket<String> bucket = redissonClient.getBucket(RedisKeys.baiduAccessTokenKey());
        if (bucket.isExists()) {
            return bucket.get();
        }

        String[] authArr = aiConfig.getAuthConfig().split(":");
        if (authArr.length != 3) {
            throw new ApiException("aiConfig.authConfig error");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        AccessTokenResponse response = restTemplate.postForObject(
                "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={1}&client_secret={2}",
                httpEntity, AccessTokenResponse.class, authArr[1], authArr[2]);

        if (response == null || StringUtils.isBlank(response.getAccessToken())) {
            throw new RuntimeException("request for access token error, response: " + response);
        }

        bucket.set(response.getAccessToken(), 20, TimeUnit.DAYS);

        return response.getAccessToken();
    }

}
