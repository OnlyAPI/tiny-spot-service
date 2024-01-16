package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/4 14:11
 */
@Data
@ApiModel(value = "角色添加/修改-请求参数")
public class RoleAddOrUpdateParam {

    @ApiModelProperty(value = "权限字符")
    @NotBlank(message = "权限字符不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "状态 0：禁用 1：启用")
    private int status;

    @ApiModelProperty(value = "菜单ids")
    private List<Long> menuIds;
}
