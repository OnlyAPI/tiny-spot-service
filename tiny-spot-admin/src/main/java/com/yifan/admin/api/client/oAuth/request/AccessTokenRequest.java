package com.yifan.admin.api.client.oAuth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 14:25
 */
@ApiModel("获取AccessToken-请求")
@Data
public class AccessTokenRequest {

    @ApiModelProperty("authorization code")
    private String code;

    @ApiModelProperty("应用ID")
    private String client_id;

    @ApiModelProperty("回掉地址")
    private String redirect_uri;

    @ApiModelProperty("应用密钥")
    private String client_secret;

}
