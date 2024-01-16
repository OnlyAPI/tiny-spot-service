package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.constant.MessageRoles;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.aliyun.ChatCompletionRequest;
import com.yifan.admin.api.model.aliyun.ChatCompletionResponse;
import com.yifan.admin.api.model.aliyun.Message;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 17:34
 */
@Slf4j
@Service
public class ALiYunChatServiceImpl implements ALiYunChatService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String getName() {
        return AiConfigTypeEnum.CHAT_ALIYUN.getName();
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
        request.setModel(authConfigArr[0]);
        request.setInput(new ChatCompletionRequest.Input(messages));
        request.setParameters(new ChatCompletionRequest.Parameters());

        Flux<ServerSentEvent<String>> eventStream = client.post()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-DashScope-SSE", "enable")
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
                if (response == null) {
                    throw new RuntimeException("service error");
                }
                if (!StringUtils.isBlank(response.getCode())) {
                    throw new RuntimeException(response.getMessage());
                }
                if (response.getOutput() != null) {
                    log.info("[doChat] outputTokens: {}, inputTokens: {}", response.getUsage().getOutputTokens(), response.getUsage().getInputTokens());
                    contentCallback.accept(StringUtils.isBlank(response.getOutput().getText()) ? "" : response.getOutput().getText());
                    if (StringUtils.equals(response.getOutput().getFinishReason(), "stop")) {
                        doneCallback.accept(resultMap);
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
                log.error("error", ex);
                errorCallback.accept(new RuntimeException(ex));
            } finally {
                MDC.clear();
            }
        });

    }


}
