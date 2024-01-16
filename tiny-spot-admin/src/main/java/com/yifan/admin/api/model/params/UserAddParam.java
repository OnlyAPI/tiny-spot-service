package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户添加参数
 */
@Getter
@Setter
@ApiModel(value = "添加用户参数")
public class UserAddParam {

    @NotEmpty(message = "invalid args [username]")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotEmpty(message = "invalid args [password]")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "用户头像")
    private String icon;

    @Email
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "角色")
    private List<Long> roleIds;

    public UserAddParam(String username, String password){
        this.username = username;
        this.password = password;
    }
    public UserAddParam(){

    }
}
