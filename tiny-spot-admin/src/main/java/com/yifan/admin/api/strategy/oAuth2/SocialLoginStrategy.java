package com.yifan.admin.api.strategy.oAuth2;

import com.yifan.admin.api.model.dto.CodeDTO;

/**
 * 第三方登录策略
 *
 * @author
 */
public interface SocialLoginStrategy {

    String authUri();

    /**
     * 登录
     *
     * @param data 第三方code
     * @return {@link String} Token
     */
    String login(CodeDTO data);
}
