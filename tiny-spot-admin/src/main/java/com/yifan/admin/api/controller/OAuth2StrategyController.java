package com.yifan.admin.api.controller;

import com.yifan.admin.api.strategy.context.SocialLoginStrategyContext;
import com.yifan.admin.api.enums.UserResourceStrategyEnum;
import com.yifan.admin.api.model.dto.CodeDTO;
import com.yifan.admin.api.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 11:19
 */
@Slf4j
@RestController
@RequestMapping("/oAuth2")
public class OAuth2StrategyController {

    @Autowired
    SocialLoginStrategyContext socialLoginStrategyContext;

    @GetMapping("/auth")
    public BaseResult<String> auth(@RequestParam(value = "authType") String authType) {
        return BaseResult.ok(socialLoginStrategyContext.executeAuthUriStrategy(UserResourceStrategyEnum.oAuth2LoginTypeEnum(authType)));
    }

    @PostMapping("/login")
    public BaseResult<String> authLogin(@RequestBody @Valid CodeDTO codeDTO) {
        return BaseResult.ok(socialLoginStrategyContext.executeLoginStrategy(codeDTO, UserResourceStrategyEnum.oAuth2LoginTypeEnum(codeDTO.getAuthType())));
    }
}
