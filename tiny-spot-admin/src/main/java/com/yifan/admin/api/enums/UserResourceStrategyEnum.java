package com.yifan.admin.api.enums;

import com.yifan.admin.api.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户来源枚举
 *
 * @author ican
 */
@Getter
@AllArgsConstructor
public enum UserResourceStrategyEnum {

    /**
     * system
     */
    SYSTEM("system", "系统登录", ""),

    /**
     * QQ
     */
    QQ("qq", "QQ登录", "qqLoginStrategy"),

    /**
     * Gitee
     */
    GITEE("gitee", "Gitee登录", "giteeLoginStrategy"),

    /**
     * Github
     */
    GITHUB("github", "Github登录", "githubLoginStrategy");


    /**
     * 用户来源
     */
    private final String resourceType;

    /**
     * 描述
     */
    private final String description;

    /**
     * 策略
     */
    private final String strategy;


    public static UserResourceStrategyEnum oAuth2LoginTypeEnum(String authType) {
        return Arrays.stream(UserResourceStrategyEnum.values()).filter(e -> {
            return e.getResourceType().equalsIgnoreCase(authType);
        }).findFirst().orElseThrow(() -> new ApiException("认证类型错误"));
    }
}
