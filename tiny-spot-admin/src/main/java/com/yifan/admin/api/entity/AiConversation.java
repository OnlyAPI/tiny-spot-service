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
 * @date 2023/12/8 15:26
 */
@Data
@TableName("ts_ai_conversation")
@ApiModel(value="AiConversation对象", description="AI会话表")
public class AiConversation {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会话ID")
    private String conversationId;

    @ApiModelProperty(value = "创建人ID")
    private Long userId;
    @ApiModelProperty(value = "机器人ID")
    private Long robotId;

    @ApiModelProperty(value = "会话名称")
    private String conversationTitle;

    @ApiModelProperty(value = "状态 0：禁用 1：可用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
