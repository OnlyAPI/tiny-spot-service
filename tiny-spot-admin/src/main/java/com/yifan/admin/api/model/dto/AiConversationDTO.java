package com.yifan.admin.api.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/8 13:13
 */
@Data
public class AiConversationDTO {
    private String conversationId;

    private String conversationTitle;

    private String avatar;

    private String configType;

    private String lastMessage;

    private Date lastMessageTime;
}
