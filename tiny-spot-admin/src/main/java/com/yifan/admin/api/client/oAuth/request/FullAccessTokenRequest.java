package com.yifan.admin.api.client.oAuth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/15 14:25
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("获取AccessToken-请求")
@Data
public class FullAccessTokenRequest extends AccessTokenRequest{

    @ApiModelProperty("授权类型")
    private String grant_type;
}
