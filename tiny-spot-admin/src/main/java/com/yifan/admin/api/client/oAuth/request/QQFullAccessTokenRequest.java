package com.yifan.admin.api.client.oAuth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/3
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("QQ获取AccessToken请求参数")
@Data
public class QQFullAccessTokenRequest extends FullAccessTokenRequest{

    @ApiModelProperty("相应格式")
    private String fmt = "json";

}
