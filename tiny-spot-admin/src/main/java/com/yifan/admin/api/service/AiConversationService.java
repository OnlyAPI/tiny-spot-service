package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.entity.AiConversation;
import com.yifan.admin.api.model.vo.ConversationVO;
import com.yifan.admin.api.model.params.CreateAiConversationParams;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/8 15:25
 */
public interface AiConversationService extends IService<AiConversation> {

    String createAiConversation(CreateAiConversationParams params);

    List<ConversationVO> getAiConversationList();

    ConversationVO getConversationInfoByCid(String cid);

    AiConversation getConversationByUserIdAndCid(Long userId, String cid);

    void delConversationByCid(String cid);
}
