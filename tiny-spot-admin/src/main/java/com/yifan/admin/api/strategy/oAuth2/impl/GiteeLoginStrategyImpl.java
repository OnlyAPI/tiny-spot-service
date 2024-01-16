package com.yifan.admin.api.strategy.oAuth2.impl;

import com.yifan.admin.api.client.oAuth.GiteeClient;
import com.yifan.admin.api.client.oAuth.request.FullAccessTokenRequest;
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
 * @date 2023/6/16 15:43
 */
@Slf4j
@Service("giteeLoginStrategy")
public class GiteeLoginStrategyImpl extends AbstractLoginStrategyImpl {

    @Autowired
    private GiteeClient giteeClient;
    @Autowired
    private OAuth2Property oAuth2Property;

    @Override
    public SocialTokenDTO getSocialToken(CodeDTO data) {
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("gitee");
        FullAccessTokenRequest request = new FullAccessTokenRequest();
        request.setClient_id(appConfig.getClientId());
        request.setClient_secret(appConfig.getClientSecret());
        request.setCode(data.getCode());
        request.setGrant_type(appConfig.getGrantType());
        request.setRedirect_uri(appConfig.getRedirectUri());

        AccessTokenByCodeResponse accessToken = giteeClient.getAccessToken(request);
        if (accessToken == null) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "Gitee登录错误");
        }
        SocialTokenDTO dto = new SocialTokenDTO();
        dto.setUserResource(UserResourceStrategyEnum.GITEE.getResourceType());
        dto.setAccessToken(accessToken.getAccess_token());
        return dto;
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialToken) {

        UserInfoByAccessTokenResponse userInfoByAccessToken = giteeClient.getUserInfoByAccessToken(socialToken.getAccessToken());
        if (userInfoByAccessToken == null) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "Gitee登录错误");
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
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("gitee");
        StringBuffer buffer = new StringBuffer(appConfig.getEndPoint())
                .append("/oauth/authorize")
                .append("?client_id=").append(appConfig.getClientId())
                .append("&redirect_uri=").append(appConfig.getRedirectUri())
                .append("&response_type=code")
                .append("&state=").append(state);
        return buffer.toString();
    }
}
