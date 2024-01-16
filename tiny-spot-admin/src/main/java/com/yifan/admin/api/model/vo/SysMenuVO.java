package com.yifan.admin.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yifan.admin.api.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/11
 */
@ApiModel
@Data
public class SysMenuVO {

    @ApiModelProperty(value = "菜单ID")
    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String title;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "权限码")
    private String permissionCode;

    @ApiModelProperty("是否菜单 1 菜单 0 按钮")
    private String isMenu;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "状态 1：可用 0：禁用")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public SysMenuVO(SysMenu menu){
        if (menu != null){
            BeanUtils.copyProperties(menu, this);
            this.isMenu = menu.getIsMenu().toString();
            this.status = menu.getStatus().toString();
        }
    }
}
