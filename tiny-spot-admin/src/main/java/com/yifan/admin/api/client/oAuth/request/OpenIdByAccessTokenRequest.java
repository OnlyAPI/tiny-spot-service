package com.yifan.admin.api.client.oAuth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@ApiModel("AccessToken获取openId-请求")
@Data
public class OpenIdByAccessTokenRequest {

    @ApiModelProperty("access_token")
    private String access_token;

    @ApiModelProperty("相应格式")
    private String fmt = "json";

}
