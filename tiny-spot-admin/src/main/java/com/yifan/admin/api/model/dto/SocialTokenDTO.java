package com.yifan.admin.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方token
 *
 * @author ican
 */
@Data
@ApiModel(description = "第三方token")
public class SocialTokenDTO {

    /**
     * 开放id
     */
    @ApiModelProperty(value = "开放id")
    private String openId;

    /**
     * 访问令牌
     */
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    /**
     * 用户来源
     */
    @ApiModelProperty(value = "用户来源")
    private String userResource;

}
