package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.AiConversationHistory;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/8
 */
public interface AiConversationHistoryService extends IService<AiConversationHistory> {

    void saveConversationHistory(Long userId, String conversationId, String userContent, String aiContent);

    AiConversationHistory queryLastConversationHistory(Long userId, String cid);

    List<AiConversationHistory> queryAiConversationHistoryListDesc(String cid);

    int delConversationHistoryByCid(Long userId, String cid);
}
