package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/4 18:08
 */
@Data
@TableName("ts_ai_robot")
@ApiModel(value = "AppAiRobot对象", description = "AI机器人表")
public class AiRobot {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "配置类型")
    private String configType;

    @ApiModelProperty(value = "机器人名称")
    private String name;

    @ApiModelProperty(value = "机器人描述")
    private String description;

    @ApiModelProperty(value = "机器人头像")
    private String avatar;

    @ApiModelProperty(value = "机器人类型 1：对话机器人 2：生图机器人")
    private Integer robotType;

    @ApiModelProperty(value = "状态 0：禁用 1：可用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
