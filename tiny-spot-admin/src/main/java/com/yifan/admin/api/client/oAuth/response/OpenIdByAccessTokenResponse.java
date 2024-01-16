package com.yifan.admin.api.client.oAuth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@Data
@ApiModel("AccessToken获取openId-响应")
public class OpenIdByAccessTokenResponse {

    @ApiModelProperty("应用ID")
    private String client_id;

    @ApiModelProperty("用户OpenID")
    private String openid;

}
