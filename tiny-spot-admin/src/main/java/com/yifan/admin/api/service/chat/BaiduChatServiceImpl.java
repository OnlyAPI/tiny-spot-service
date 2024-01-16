package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.constant.MessageRoles;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.model.baidu.ChatCompletionRequest;
import com.yifan.admin.api.model.baidu.ChatCompletionResponse;
import com.yifan.admin.api.model.baidu.Message;
import com.yifan.admin.api.service.AbstractBaiduService;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
 * @date 2023/11/15 14:42
 */
@Slf4j
@Service
public class BaiduChatServiceImpl extends AbstractBaiduService implements BaiduChatService {

    public BaiduChatServiceImpl(RedissonClient redissonClient, RestTemplate restTemplate) {
        super(redissonClient, restTemplate);
    }

    @Override
    public String getName() {
        return AiConfigTypeEnum.CHAT_BAIDU.getName();
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

        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(messages);
        request.setStream(true);

        Flux<ServerSentEvent<String>> eventStream = client.post()
                .uri("?access_token=" + getAccessToken(aiConfig))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
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
                if (response != null) {
                    contentCallback.accept(StringUtils.isBlank(response.getResult()) ? "" : response.getResult());
                    if (response.getIsEnd() != null && response.getIsEnd()) {
                        doneCallback.accept(resultMap);
                        return;
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
