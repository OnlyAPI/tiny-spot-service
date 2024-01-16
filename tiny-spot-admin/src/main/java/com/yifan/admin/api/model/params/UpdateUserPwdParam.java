package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 修改用户名密码参数
 */
@Getter
@Setter
public class UpdateUserPwdParam {

    //@NotEmpty(message = "用户名不能为空")
//    @ApiModelProperty(value = "用户名", required = true)
//    private String username;

    @NotEmpty(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;

    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}
