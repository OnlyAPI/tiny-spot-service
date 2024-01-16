package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 后台用户和角色关系表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_sys_user_role")
@ApiModel(value="SysUserRole对象", description="用户和角色关系表")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long adminId;

    @ApiModelProperty("角色ID")
    private Long roleId;


}
