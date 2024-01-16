package com.yifan.admin.api.client.oAuth;

import com.yifan.admin.api.client.oAuth.request.FullAccessTokenRequest;
import com.yifan.admin.api.client.oAuth.response.AccessTokenByCodeResponse;
import com.yifan.admin.api.client.oAuth.response.UserInfoByAccessTokenResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 12:32
 */
public interface GiteeClient {

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @RequestLine("POST /oauth/token")
    AccessTokenByCodeResponse getAccessToken(FullAccessTokenRequest request);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @RequestLine("GET /api/v5/user?access_token={accessToken}")
    UserInfoByAccessTokenResponse getUserInfoByAccessToken(@Param("accessToken") String accessToken);
}
