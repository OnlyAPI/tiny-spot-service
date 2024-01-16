package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/17 11:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_plan")
@ApiModel(value = "Plan对象", description = "计划表")
public class Plan {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("计划描述")
    private String planDesc;

    @ApiModelProperty("计划日期")
    private String planDate;

    @ApiModelProperty("状态 0：未完成 1： 已完成 2：删除")
    private Integer status;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
