package com.yifan.admin.api.strategy.context;

import com.yifan.admin.api.strategy.oAuth2.SocialLoginStrategy;
import com.yifan.admin.api.enums.UserResourceStrategyEnum;
import com.yifan.admin.api.model.dto.CodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录策略上下文
 *
 * @author ican
 */
@Component
public class SocialLoginStrategyContext {

    @Autowired
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 登录
     *
     * @param data          data
     * @param loginTypeEnum 登录枚举
     * @return {@link String} Token
     */
    public String executeLoginStrategy(CodeDTO data, UserResourceStrategyEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

    /**
     * 获取重定向地址
     * @param loginTypeEnum
     * @return
     */
    public String executeAuthUriStrategy(UserResourceStrategyEnum loginTypeEnum){
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).authUri();
    }
}
