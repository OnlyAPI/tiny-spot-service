package com.yifan.admin.api.client.oAuth;

import com.yifan.admin.api.client.oAuth.request.OpenIdByAccessTokenRequest;
import com.yifan.admin.api.client.oAuth.request.QQFullAccessTokenRequest;
import com.yifan.admin.api.client.oAuth.request.UserInfoByOpenIdAndAccTokenRequest;
import com.yifan.admin.api.client.oAuth.response.AccessTokenByCodeResponse;
import com.yifan.admin.api.client.oAuth.response.OpenIdByAccessTokenResponse;
import com.yifan.admin.api.client.oAuth.response.UserInfoByOpenIdAndAccTokenResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
public interface QQApiClient {

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @RequestLine("GET /oauth2.0/token")
    AccessTokenByCodeResponse getAccessToken(QQFullAccessTokenRequest request);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @RequestLine("GET /oauth2.0/me")
    OpenIdByAccessTokenResponse getOpenIdByAccessToken(OpenIdByAccessTokenRequest request);

    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @RequestLine("GET /user/get_user_info")
    UserInfoByOpenIdAndAccTokenResponse getUserInfoByOpenIdAndAccToken(UserInfoByOpenIdAndAccTokenRequest request);
}
