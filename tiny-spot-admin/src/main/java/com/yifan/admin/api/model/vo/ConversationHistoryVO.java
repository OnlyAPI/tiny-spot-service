package com.yifan.admin.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationHistoryVO {

    private String messageId;

    private String content;

    private Boolean isMine;

    private String time;
}
