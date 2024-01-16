package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录参数
 */
@Data
@ApiModel(value = "系统用户登录请求参数")
public class CommonLoginParam {

    @NotEmpty(message = "invalid args [username]")
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @NotEmpty(message = "invalid args [password]")
    @ApiModelProperty(value = "密码",required = true)
    @Length(min = 6, max = 20, message = "invalid args [password]")
    private String password;

    @NotEmpty(message = "invalid args [code]")
    @ApiModelProperty(value = "验证码",required = true)
    private String code;

    @NotEmpty(message = "invalid args [uuid]")
    @ApiModelProperty(value = "获取验证码时携带的随机数",required = true)
    private String uuid;
}
