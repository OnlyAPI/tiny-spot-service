package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 11:57
 */
@Data
@ApiModel("给用户分配角色请求参数")
public class AdminAllocRoleParam {

    @NotNull(message = "invalid args [adminId]")
    @ApiModelProperty("userId")
    private Long userId;

    //@NotBlank(message = "invalid args [roleIds]")
    @ApiModelProperty("角色id集合")
    private String roleIds;

}
