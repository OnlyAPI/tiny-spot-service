package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.enums.WsMsgTypeEnum;
import com.yifan.admin.api.model.dto.AiConversationDTO;
import com.yifan.admin.api.utils.DateTimeUtil;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/7 18:59
 */
@Data
public class ConversationVO {

    private String conversationId;

    private String name;

    private String avatar;

    private String lastMessage;

    private String lastMessageTime;

    private String mark;

    private String type;

    private Boolean isAi;


    public ConversationVO(AiConversationDTO dto) {
        if (dto != null) {
            this.conversationId = dto.getConversationId();
            this.name = dto.getConversationTitle();
            this.avatar = dto.getAvatar();
            this.lastMessage = dto.getLastMessage();
            this.lastMessageTime = DateTimeUtil.formatConversationLastMessageTime(dto.getLastMessageTime());
            this.mark = dto.getConfigType();
            this.type = WsMsgTypeEnum.REQ_AI_CHAT_MSG.getType();
            this.isAi = true;
        }
    }

}
