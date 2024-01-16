package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.constant.MessageRoles;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.model.byteDance.ChatCompletionParams;
import com.yifan.admin.api.model.byteDance.ChatStreamResponse;
import com.yifan.admin.api.model.byteDance.Message;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.utils.SkyLarkSignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/13
 */
@Slf4j
@Service
public class SkylarkChatServiceImpl implements SkylarkChatService{
    @Override
    public String getName() {
        return AiConfigTypeEnum.CHAT_SKYLARK.getName();
    }

    @Override
    public boolean isSupported(AiConfig aiConfig) {
        return aiConfig != null && StringUtils.equals(aiConfig.getConfigType(), getName());
    }

    @Override
    public void simpleStreamChat(AiConfig aiConfig, String prompt, Consumer<Map<String, Object>> doneCallback, Consumer<String> contentCallback, Consumer<Exception> errorCallback) {
        log.info("[simpleStreamChat] prompt: {}", prompt);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(MessageRoles.USER, prompt));
        Map<String, Object> resultMap = new HashMap<>();
        doChat(aiConfig, messages, resultMap, doneCallback, contentCallback, errorCallback);
    }

    private void doChat(AiConfig aiConfig,
                        List<Message> messages,
                        Map<String, Object> resultMap,
                        Consumer<Map<String, Object>> doneCallback,
                        Consumer<String> contentCallback,
                        Consumer<Exception> errorCallback) {

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl(aiConfig.getServerUrl())
                .build();
        ParameterizedTypeReference<ServerSentEvent<String>> bodyType = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        final String[] authConfigArr = aiConfig.getAuthConfig().split(":");
        ChatCompletionParams.Model model = new ChatCompletionParams.Model();
        model.setName(authConfigArr[0]);

        ChatCompletionParams params = new ChatCompletionParams();
        params.setStream(true);
        params.setMessages(messages);
        params.setModel(model);

        final String path = "/api/v1/chat";

        Map<String, String> signMap;
        try {
            signMap = SkyLarkSignUtils.getSkylarkSign(aiConfig.getServerUrl(), path,
                    authConfigArr[1], authConfigArr[2],
                    JacksonJsonUtil.toJson(params).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("sign error");
            errorCallback.accept(new RuntimeException("service error"));
            return;
        }

        Flux<ServerSentEvent<String>> eventStream = client.post()
                .uri(path)
                .header("Content-Type", signMap.get("Content-Type"))
                .header("Host", signMap.get("Host"))
                .header("X-Date", signMap.get("X-Date"))
                .header("X-Content-Sha256", signMap.get("X-Content-Sha256"))
                .header("Authorization", signMap.get("Authorization"))
                .bodyValue(params)
                .retrieve()
                .bodyToFlux(bodyType);

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        eventStream.subscribe(content -> {
            if (mdcContext != null) {
                MDC.setContextMap(mdcContext);
            }
            try {
                if (StringUtils.equals(content.data(), "[DONE]")) {
//                    doneCallback.accept(resultMap);
                    return;
                }
                ChatStreamResponse response = JacksonJsonUtil.fromJson(content.data(), ChatStreamResponse.class);
                if (response != null) {
                    if (response.getError() != null && !StringUtils.isBlank(response.getError().getMessage())){
                        log.error("error: {}", JacksonJsonUtil.toJson(response.getError()));
                        errorCallback.accept(new RuntimeException("service error"));
                        return;
                    }
                    ChatStreamResponse.Choice choice = response.getChoice();
                    contentCallback.accept(StringUtils.isBlank(choice.getMessage().getContent())? "" : choice.getMessage().getContent());
                    if (StringUtils.equals(choice.getFinishReason(), "stop")) {
                        doneCallback.accept(resultMap);
                        log.info("chat completed, Usage: {}", JacksonJsonUtil.toJson(response.getUsage()));
                    }
                }
            } catch (Exception e) {
                errorCallback.accept(e);
            } finally {
                MDC.clear();
            }
        }, ex -> {
            if (mdcContext != null) {
                MDC.setContextMap(mdcContext);
            }
            try {
                errorCallback.accept(new RuntimeException(ex));
            } finally {
                MDC.clear();
            }
        });
    }
}
