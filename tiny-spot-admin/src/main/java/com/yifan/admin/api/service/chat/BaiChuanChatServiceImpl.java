package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.constant.MessageRoles;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.baichuan.ChatCompletionRequest;
import com.yifan.admin.api.model.baichuan.ChatCompletionResponse;
import com.yifan.admin.api.model.baichuan.Message;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/8 16:31
 */
@Slf4j
@Service
public class BaiChuanChatServiceImpl implements BaiChuanChatService {


    @Override
    public String getName() {
        return AiConfigTypeEnum.CHAT_BAICHUAN.getName();
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
        HashMap<String, Object> resultMap = new HashMap<>();
        doChat(aiConfig, messages, resultMap, doneCallback, contentCallback, errorCallback);
    }

    private void doChat(AiConfig aiConfig,
                        List<Message> messages,
                        HashMap<String, Object> resultMap,
                        Consumer<Map<String, Object>> doneCallback,
                        Consumer<String> contentCallback,
                        Consumer<Exception> errorCallback) {

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl(aiConfig.getServerUrl())
                .build();

        ParameterizedTypeReference<ServerSentEvent<String>> bodyType = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        if (StringUtils.isBlank(aiConfig.getAuthConfig())) {
            throw new ApiException("authConfig error");
        }

        String[] authConfigArr = aiConfig.getAuthConfig().split(":");

        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setStream(true);
        request.setMessages(messages);
        request.setModel(authConfigArr[0]);

        Flux<ServerSentEvent<String>> eventStream = client.post()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + authConfigArr[1])
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(bodyType);

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        eventStream.subscribe(content -> {
            if (mdcContext != null) {
                MDC.setContextMap(mdcContext);
            }
            try {
                if (StringUtils.equals(content.data(), "[DONE]")) {
                    doneCallback.accept(resultMap);
                    return;
                }
                ChatCompletionResponse response = JacksonJsonUtil.fromJson(content.data(), ChatCompletionResponse.class);
                if (response != null && !response.getChoices().isEmpty()) {
                    response.getChoices().forEach(choices -> {
                        Message message = choices.getMessage();
                        contentCallback.accept(StringUtils.isBlank(message.getContent()) ? "" : message.getContent());
                    });
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
