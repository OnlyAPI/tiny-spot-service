package com.yifan.admin.api.controller;

import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.model.data.StoreWsTokenData;
import com.yifan.admin.api.model.vo.WSTokenVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/wstoken")
public class WsTokenController {
    @Resource
    CacheService cacheService;

    @PostMapping
    public BaseResult<WSTokenVO> getWSToken() {

        String wstoken = "";
        do {
            wstoken = TokenUtil.token(System.currentTimeMillis(), RandomStringUtils.random(32, true, true));
        } while (cacheService.existWsToken(wstoken));

        cacheService.cacheWsTokenInfo(wstoken, new StoreWsTokenData(String.valueOf(RequestContext.getUser().getId())));

        WSTokenVO wsTokenDto = new WSTokenVO();
        wsTokenDto.setWstoken(wstoken);
        return BaseResult.ok(wsTokenDto);
    }

}
