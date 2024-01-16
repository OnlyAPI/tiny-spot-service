package com.yifan.admin.api.client.oAuth;

import com.yifan.admin.api.client.oAuth.request.AccessTokenRequest;
import com.yifan.admin.api.client.oAuth.response.AccessTokenByCodeResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/19 10:54
 */
public interface GithubClient {

    @Headers({"Accept:application/json", "Content-Type:application/json;charset=UTF-8"})
    @RequestLine("POST /login/oauth/access_token")
    AccessTokenByCodeResponse getAccessToken(AccessTokenRequest request);


}
