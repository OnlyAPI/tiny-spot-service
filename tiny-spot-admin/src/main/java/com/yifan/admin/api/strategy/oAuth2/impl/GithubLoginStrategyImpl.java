package com.yifan.admin.api.strategy.oAuth2.impl;

import com.yifan.admin.api.client.oAuth.GithubApiClient;
import com.yifan.admin.api.client.oAuth.GithubClient;
import com.yifan.admin.api.client.oAuth.request.AccessTokenRequest;
import com.yifan.admin.api.client.oAuth.response.AccessTokenByCodeResponse;
import com.yifan.admin.api.client.oAuth.response.UserInfoByAccessTokenResponse;
import com.yifan.admin.api.config.properties.OAuth2Property;
import com.yifan.admin.api.enums.UserResourceStrategyEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.dto.CodeDTO;
import com.yifan.admin.api.model.dto.SocialTokenDTO;
import com.yifan.admin.api.model.dto.SocialUserInfoDTO;
import com.yifan.admin.api.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/19 11:06
 */
@Slf4j
@Service("githubLoginStrategy")
public class GithubLoginStrategyImpl extends AbstractLoginStrategyImpl {

    @Autowired
    private GithubClient githubClient;
    @Autowired
    private GithubApiClient githubApiClient;
    @Autowired
    private OAuth2Property oAuth2Property;

    @Override
    public SocialTokenDTO getSocialToken(CodeDTO data) {
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("github");
        AccessTokenRequest request = new AccessTokenRequest();
        request.setClient_id(appConfig.getClientId());
        request.setClient_secret(appConfig.getClientSecret());
        request.setCode(data.getCode());
        request.setRedirect_uri(appConfig.getRedirectUri());

        AccessTokenByCodeResponse accessToken = githubClient.getAccessToken(request);

        if (accessToken == null) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "Github登录错误");
        }
        SocialTokenDTO dto = new SocialTokenDTO();
        dto.setUserResource(UserResourceStrategyEnum.GITHUB.getResourceType());
        dto.setAccessToken(accessToken.getAccess_token());
        return dto;
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialToken) {
        UserInfoByAccessTokenResponse userInfoByAccessToken = githubApiClient.getUserInfoByAccessToken(socialToken.getAccessToken());
        if (userInfoByAccessToken == null) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "Github登录错误");
        }
        SocialUserInfoDTO infoDTO = new SocialUserInfoDTO();
        infoDTO.setId(userInfoByAccessToken.getId());
        infoDTO.setAvatar(userInfoByAccessToken.getAvatar_url());
        infoDTO.setNickname(userInfoByAccessToken.getName());
        infoDTO.setUsername(userInfoByAccessToken.getLogin());
        return infoDTO;
    }

    @Override
    public String getSocialAuthUri(String state) {
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("github");
        StringBuffer buffer = new StringBuffer(appConfig.getEndPoint())
                .append("/login/oauth/authorize")
                .append("?client_id=").append(appConfig.getClientId())
                .append("&redirect_uri=").append(appConfig.getRedirectUri())
                .append("&scope=user")
                .append("&response_type=code")
                .append("&state=").append(state);;
        return buffer.toString();
    }
}
