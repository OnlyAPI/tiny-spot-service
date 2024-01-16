package com.yifan.admin.api.client.oAuth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@ApiModel("通过OpenID获取用户信息-请求")
@Data
public class UserInfoByOpenIdAndAccTokenRequest {

    @ApiModelProperty("access_token")
    private String access_token;

    @ApiModelProperty("申请QQ登录成功后，分配给应用的appid")
    private String oauth_consumer_key;

    @ApiModelProperty("用户的ID")
    private String openid;

}
