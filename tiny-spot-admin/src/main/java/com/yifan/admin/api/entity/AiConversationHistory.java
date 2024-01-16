package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/8
 */
@Data
@TableName("ts_ai_conversation_history")
@ApiModel(value = "AiConversationHistory对象", description = "AI会话历史表")
public class AiConversationHistory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String conversationId;

    private String userContent;

    private String aiContent;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
