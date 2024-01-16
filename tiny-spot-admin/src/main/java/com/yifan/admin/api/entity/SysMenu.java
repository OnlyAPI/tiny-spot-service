package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台菜单表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_sys_menu")
@ApiModel(value="SysMenu对象", description="菜单表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
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
    private Integer isMenu;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "状态 1：可用 0：禁用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
