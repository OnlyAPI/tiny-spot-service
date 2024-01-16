package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="UpdateSysUserInfoParam对象", description="修改系统用户信息参数")
public class UpdateSysUserInfoParam extends UpdateUserInfoParam{

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "状态 1：禁用 1：启用")
    private int status;

    @ApiModelProperty(value = "角色id")
    private List<Long> roleIds;
}
