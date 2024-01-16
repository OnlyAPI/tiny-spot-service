package com.yifan.admin.api.strategy.oAuth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yifan.admin.api.client.oAuth.QQApiClient;
import com.yifan.admin.api.client.oAuth.request.OpenIdByAccessTokenRequest;
import com.yifan.admin.api.client.oAuth.request.QQFullAccessTokenRequest;
import com.yifan.admin.api.client.oAuth.request.UserInfoByOpenIdAndAccTokenRequest;
import com.yifan.admin.api.client.oAuth.response.AccessTokenByCodeResponse;
import com.yifan.admin.api.client.oAuth.response.OpenIdByAccessTokenResponse;
import com.yifan.admin.api.client.oAuth.response.UserInfoByOpenIdAndAccTokenResponse;
import com.yifan.admin.api.config.properties.OAuth2Property;
import com.yifan.admin.api.mapper.SysUserMapper;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.UserResourceStrategyEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.dto.CodeDTO;
import com.yifan.admin.api.model.dto.SocialTokenDTO;
import com.yifan.admin.api.model.dto.SocialUserInfoDTO;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@Slf4j
@Service(value = "qqLoginStrategy")
public class QQLoginStrategyImpl extends AbstractLoginStrategyImpl {

    @Autowired
    private OAuth2Property oAuth2Property;
    @Autowired
    private QQApiClient qqApiClient;
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SocialTokenDTO getSocialToken(CodeDTO data) {
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("qq");

        QQFullAccessTokenRequest request = new QQFullAccessTokenRequest();
        request.setClient_id(appConfig.getClientId());
        request.setClient_secret(appConfig.getClientSecret());
        request.setRedirect_uri(appConfig.getRedirectUri());
        request.setGrant_type(appConfig.getGrantType());
        request.setCode(data.getCode());

        log.info("[qq-getSocialToken] getAccessToken request params: {}.", JacksonJsonUtil.toJson(request));
        AccessTokenByCodeResponse accessTokenByCodeResponse = qqApiClient.getAccessToken(request);
        log.info("[qq-getSocialToken] getAccessToken response params: {}.", JacksonJsonUtil.toJson(accessTokenByCodeResponse));

        if (accessTokenByCodeResponse == null || StringUtils.isBlank(accessTokenByCodeResponse.getAccess_token())) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "QQ登录错误");
        }

        OpenIdByAccessTokenRequest request1 = new OpenIdByAccessTokenRequest();
        request1.setAccess_token(accessTokenByCodeResponse.getAccess_token());

        log.info("[qq-getSocialToken] getOpenId request params: {}.", JacksonJsonUtil.toJson(request));
        OpenIdByAccessTokenResponse openIdByAccessTokenResponse = qqApiClient.getOpenIdByAccessToken(request1);
        log.info("[qq-getSocialToken] getOpenId response params: {}.", JacksonJsonUtil.toJson(openIdByAccessTokenResponse));

        if (openIdByAccessTokenResponse == null) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "QQ登录错误");
        }

        SocialTokenDTO dto = new SocialTokenDTO();
        dto.setAccessToken(accessTokenByCodeResponse.getAccess_token());
        dto.setOpenId(openIdByAccessTokenResponse.getOpenid());
        dto.setUserResource(UserResourceStrategyEnum.QQ.getResourceType());
        return dto;
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialToken) {

        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("qq");

        UserInfoByOpenIdAndAccTokenRequest request = new UserInfoByOpenIdAndAccTokenRequest();
        request.setOpenid(socialToken.getOpenId());
        request.setAccess_token(socialToken.getAccessToken());
        request.setOauth_consumer_key(appConfig.getClientId());

        log.info("[qq-getSocialUserInfo] request params: {}.", JacksonJsonUtil.toJson(request));
        UserInfoByOpenIdAndAccTokenResponse userInfoResponse = qqApiClient.getUserInfoByOpenIdAndAccToken(request);
        log.info("[qq-getSocialUserInfo] response params: {}.", JacksonJsonUtil.toJson(userInfoResponse));

        if (!Objects.equals(userInfoResponse.getRet(), 0)) {
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "QQ登录错误");
        }

        LambdaQueryWrapper<SysUser> lambda = new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getOpenId, socialToken.getOpenId())
                .eq(SysUser::getUserResource, socialToken.getUserResource());
        SysUser user = userMapper.selectOne(lambda);

        SocialUserInfoDTO dto = new SocialUserInfoDTO();
        if (user == null) {
            dto.setAvatar(userInfoResponse.getFigureurl());
            dto.setUsername(buildUserName(socialToken.getUserResource()));
            dto.setNickname(userInfoResponse.getNickname());
        } else {
            dto.setAvatar(user.getIcon());
            dto.setUsername(user.getUsername());
            dto.setNickname(user.getNickName());
        }
        return dto;
    }

    private String buildUserName(String userResource) {
        return String.join("_", userResource, RandomStringUtils.random(6, true, true));
    }

    @Override
    public String getSocialAuthUri(String state) {
        OAuth2Property.AppConfig appConfig = oAuth2Property.getAppConfig("qq");
        StringBuilder buffer = new StringBuilder(appConfig.getEndPoint());
        try {
            buffer.append("/oauth2.0/authorize")
                    .append("?client_id=").append(appConfig.getClientId())
                    .append("&redirect_uri=").append(URLEncoder.encode(appConfig.getRedirectUri(), "UTF-8"))
                    .append("&state=").append(state)
                    .append("&scope=get_user_info")
                    .append("&response_type=code");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
