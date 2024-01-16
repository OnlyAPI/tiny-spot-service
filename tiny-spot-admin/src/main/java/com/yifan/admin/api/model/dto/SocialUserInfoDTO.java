package com.yifan.admin.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方账号信息
 *
 * @author ican
 */
@Data
@ApiModel(description = "第三方账号信息")
public class SocialUserInfoDTO {

    @ApiModelProperty("第三方uId")
    private Integer id;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty("登录名称")
    private String username;
}