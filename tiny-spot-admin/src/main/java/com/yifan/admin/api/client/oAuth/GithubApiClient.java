package com.yifan.admin.api.client.oAuth;

import com.yifan.admin.api.client.oAuth.response.UserInfoByAccessTokenResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/19 11:55
 */
public interface GithubApiClient {

    @Headers({"Accept:application/json", "Content-Type:application/json;charset=UTF-8", "Authorization: Bearer {accessToken}"})
    @RequestLine("GET /user")
    UserInfoByAccessTokenResponse getUserInfoByAccessToken(@Param("accessToken") String accessToken);

}
