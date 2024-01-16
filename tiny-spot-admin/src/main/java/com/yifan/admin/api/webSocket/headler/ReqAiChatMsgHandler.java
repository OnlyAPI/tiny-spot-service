package com.yifan.admin.api.webSocket.headler;

import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.entity.AiConversation;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.service.AiConfigService;
import com.yifan.admin.api.service.AiConversationHistoryService;
import com.yifan.admin.api.service.AiConversationService;
import com.yifan.admin.api.service.chat.ChatService;
import com.yifan.admin.api.utils.CollectionsUtil;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.webSocket.WebSocketContext;
import com.yifan.admin.api.webSocket.msg.req.ReqAiChatMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespAiChatMsg;
import com.yifan.admin.api.webSocket.msg.resp.RespErrorMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/27 18:34
 */
@Component
@Slf4j
public class ReqAiChatMsgHandler implements WebsocketMsgHandler<ReqAiChatMsg> {

    @Resource
    private List<ChatService> chatServices;
    @Resource
    private AiConfigService aiConfigService;
    @Resource
    private AiConversationService aiConversationService;
    @Resource
    private AiConversationHistoryService aiConversationHistoryService;

    private final ExecutorService executorService;

    public ReqAiChatMsgHandler() {
        this.executorService = new ThreadPoolExecutor(10, 50, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new CustomizableThreadFactory("ai-chat-"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void shutdownExecutor() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            log.error("shutdown executorService error", e);
            executorService.shutdownNow();
        }
    }

    @Override
    public void handle(Session session, ReqAiChatMsg msg) throws Exception {
        if (StringUtils.isBlank(msg.getMark()) || StringUtils.isBlank(msg.getConversationId())){
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("参数非法")));
            return;
        }
        AiConfigTypeEnum configTypeEnum = AiConfigTypeEnum.fromName(msg.getMark());
        if (configTypeEnum == null) {
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("参数[mark]非法")));
            return;
        }

        final Long userId = Long.valueOf(WebSocketContext.getUId(session));
        AiConversation conversation = aiConversationService.getConversationByUserIdAndCid(userId, msg.getConversationId());
        if (conversation == null) {
            WebSocketContext.sendMsg(session,JacksonJsonUtil.toJson(new RespErrorMsg("参数[conversationId]非法")));
            return;
        }

        AiConfig aiConfig = CollectionsUtil.getRandom(aiConfigService.getUsableConfigByType(configTypeEnum));
        if (aiConfig == null) {
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("参数[mark]非法")));
            return;
        }

        boolean hasChatServiceSupported = false;
        StringBuilder builder = new StringBuilder();
        for (ChatService chatService : chatServices) {
            if (chatService.isSupported(aiConfig)) {
                builder.setLength(0);
                chatService.simpleStreamChat(aiConfig, msg.getText(), (map) -> {
                    log.info("chat completed, userId: {}, question: {}", WebSocketContext.getUId(session), msg.getText());
                    WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespAiChatMsg("[END]")));
                    if (builder.length() > 0) {
                        log.info("chat completed，question: {}, answer: {}", msg.getText(), builder.toString());
                    }
                    executorService.submit(() -> {
                        aiConversationHistoryService.saveConversationHistory(userId, msg.getConversationId(), msg.getText(), builder.toString());
                    });
                }, content -> {
                    builder.append(content);
                    WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespAiChatMsg(StringUtils.isBlank(content) ? "" : content)));
                }, ex -> {
                    log.info("chat error. userId: {}, prompt: {}.", WebSocketContext.getUId(session), msg.getText(), ex);
                    WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg(ex.getMessage())));
                });
                hasChatServiceSupported = true;
                break;
            }
        }

        if (!hasChatServiceSupported) {
            WebSocketContext.sendMsg(session, JacksonJsonUtil.toJson(new RespErrorMsg("参数[mark]非法")));
        }
    }
}
