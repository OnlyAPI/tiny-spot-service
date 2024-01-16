package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.AiConversation;
import com.yifan.admin.api.entity.AiConversationHistory;
import com.yifan.admin.api.entity.AiRobot;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.mapper.AiConversationMapper;
import com.yifan.admin.api.model.dto.AiConversationDTO;
import com.yifan.admin.api.model.params.CreateAiConversationParams;
import com.yifan.admin.api.model.vo.ConversationVO;
import com.yifan.admin.api.service.AiConversationHistoryService;
import com.yifan.admin.api.service.AiConversationService;
import com.yifan.admin.api.service.AiRobotService;
import com.yifan.admin.api.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/8 15:27
 */
@Slf4j
@Service
public class AiConversationServiceImpl extends ServiceImpl<AiConversationMapper, AiConversation> implements AiConversationService {

    @Resource
    private AiRobotService aiRobotService;
    @Resource
    private AiConversationHistoryService aiConversationHistoryService;

    @Override
    public String createAiConversation(CreateAiConversationParams params) {
        log.info("[createAiConversation] robotId: {}, name: {}", params.getRobotId(), params.getName());

        AiRobot robot = aiRobotService.getRobotById(params.getRobotId());
        if (robot == null) {
            log.error("[createAiConversation] not found robot id: {}", params.getRobotId());
            throw new ApiException("无效参数");
        }

        final Long userId = RequestContext.getUser().getId();
        final String cId = "CID-" + RandomStringUtils.random(32, true, true) + DateTimeUtil.currentMilli();

        AiConversation aiConversation = new AiConversation();
        aiConversation.setConversationId(cId);
        aiConversation.setConversationTitle(StringUtils.isNotBlank(params.getName()) ? params.getName() : robot.getName());
        aiConversation.setRobotId(params.getRobotId());
        aiConversation.setUserId(userId);
        aiConversation.setStatus(StatusEnum.USABLE.getStatus());
        aiConversation.setCreateTime(DateTimeUtil.currentDate());
        aiConversation.setUpdateTime(aiConversation.getCreateTime());

        baseMapper.insert(aiConversation);
        return cId;
    }

    @Override
    public List<ConversationVO> getAiConversationList() {
        final Long userId = RequestContext.getUser().getId();
        List<AiConversationDTO> conversationDTOList = baseMapper.queryAiConversationsByUserId(userId);
        if (conversationDTOList.isEmpty()) {
            return Collections.emptyList();
        }
        return conversationDTOList.stream().map(c -> {
            AiConversationHistory history = aiConversationHistoryService.queryLastConversationHistory(userId, c.getConversationId());
            c.setLastMessage(history != null ? history.getUserContent() : "");
            c.setLastMessageTime(history != null ? history.getCreateTime() : null);
            return new ConversationVO(c);
        }).collect(Collectors.toList());
    }

    @Override
    public ConversationVO getConversationInfoByCid(String cid) {
        final Long userId = RequestContext.getUser().getId();
        AiConversationDTO conversationDTO = baseMapper.queryAiConversationByUserIdAndCId(userId, cid);
        AiConversationHistory history = aiConversationHistoryService.queryLastConversationHistory(userId, cid);
        conversationDTO.setLastMessage(history != null ? history.getUserContent() : "");
        conversationDTO.setLastMessageTime(history != null ? history.getCreateTime() : null);

        return new ConversationVO(conversationDTO);
    }

    @Override
    public AiConversation getConversationByUserIdAndCid(Long userId, String cid) {
        return baseMapper.getUsableConversationByUserIdAndCid(userId, cid);
    }

    @Override
    @Transactional
    public void delConversationByCid(String cid) {
        log.info("delete conversation, cid: {}", cid);
        final Long userId = RequestContext.getUser().getId();
        int delResult = baseMapper.delConversationByCid(userId, cid);
        int delHistoryResult = aiConversationHistoryService.delConversationHistoryByCid(userId, cid);
        log.info("delete conversation result, conversation: {}, history:{}", delResult, delHistoryResult);
    }
}
