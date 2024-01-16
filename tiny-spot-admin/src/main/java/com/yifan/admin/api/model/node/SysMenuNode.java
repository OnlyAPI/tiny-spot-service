package com.yifan.admin.api.model.node;


import com.yifan.admin.api.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 */
@Getter
@Setter
@ApiModel(value = "后台菜单节点封装")
public class SysMenuNode extends SysMenu {

    @ApiModelProperty(value = "子级菜单")
    private List<SysMenuNode> children;

}
