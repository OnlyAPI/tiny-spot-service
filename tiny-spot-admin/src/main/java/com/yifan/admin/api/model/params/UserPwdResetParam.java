package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 13:59
 */
@Data
@ApiModel(value = "重置密码参数")
public class UserPwdResetParam {

    @ApiModelProperty(value = "新密码")
    private String newPwd;

}
