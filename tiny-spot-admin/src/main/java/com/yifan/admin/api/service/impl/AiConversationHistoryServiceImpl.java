package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.AiConversationHistory;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.mapper.AiConversationHistoryMapper;
import com.yifan.admin.api.service.AiConversationHistoryService;
import com.yifan.admin.api.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/8
 */
@Slf4j
@Service
public class AiConversationHistoryServiceImpl extends ServiceImpl<AiConversationHistoryMapper, AiConversationHistory> implements AiConversationHistoryService {

    @Override
    public void saveConversationHistory(Long userId, String conversationId, String userContent, String aiContent) {
        log.info("[saveConversationHistory] userId: {}, cid: {}, userContent: {}, aiContent: {}", userId, conversationId, userContent, aiContent);
        AiConversationHistory history = new AiConversationHistory();
        history.setConversationId(conversationId);
        history.setUserId(userId);
        history.setAiContent(aiContent);
        history.setUserContent(userContent);
        history.setStatus(StatusEnum.USABLE.getStatus());
        history.setCreateTime(DateTimeUtil.currentDate());
        history.setUpdateTime(history.getCreateTime());
        baseMapper.insert(history);
        log.info("[saveConversationHistory] save success");
    }

    @Override
    public AiConversationHistory queryLastConversationHistory(Long userId, String cid) {
        return baseMapper.queryLastConversationHistory(userId, cid);
    }

    @Override
    public List<AiConversationHistory> queryAiConversationHistoryListDesc(String cid) {
        final Long userId = RequestContext.getUser().getId();
        return baseMapper.queryAiConversationHistoryListDesc(userId, cid);
    }

    @Override
    public int delConversationHistoryByCid(Long userId, String cid) {
        return baseMapper.delConversationHistoryByCid(userId, cid);
    }
}
