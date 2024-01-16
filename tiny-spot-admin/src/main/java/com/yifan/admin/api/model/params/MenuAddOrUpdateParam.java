package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/11
 */
@Data
@ApiModel(value = "菜单添加或修改请求参数")
public class MenuAddOrUpdateParam {

    @ApiModelProperty(value = "父级ID")
    @NotNull(message = "父级ID不能为空")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    @ApiModelProperty(value = "菜单排序")
    @NotNull(message = "菜单排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "权限码")
    private String permissionCode;

    @ApiModelProperty("是否菜单 0:按钮 1:菜单 2:目录 3：外链")
    @NotNull(message = "类型不能为空")
    private Integer isMenu;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "状态 1：可用 0：禁用")
    private Integer status;

}
