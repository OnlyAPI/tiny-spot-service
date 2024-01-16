package com.yifan.admin.api.model.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/5 15:15
 */
@ApiModel(value = "角色菜单树结点")
@Data
public class RoleMenuTreeNode {

    @ApiModelProperty(value = "菜单id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String label;

    @ApiModelProperty(value = "子节点")
    private List<RoleMenuTreeNode> children;

}
