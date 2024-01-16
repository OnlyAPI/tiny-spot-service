package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/14 15:16
 */
@Data
@ApiModel(value="UpdateUserInfo对象", description="修改自己信息参数")
public class UpdateUserInfoParam {

//    @ApiModelProperty(value = "userId")
//    private Long id;
//
//    @ApiModelProperty(value = "密码")
//    private String password;
//
//    @ApiModelProperty(value = "头像")
//    private String icon;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "email can not be null")
    private String email;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "nickName can not be null")
    private String nickName;

    @ApiModelProperty(value = "备注信息")
    private String note;

}
