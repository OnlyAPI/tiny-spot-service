package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.model.xunfei.SparkDeskClient;
import com.yifan.admin.api.constant.MessageRoles;
import com.yifan.admin.api.entity.AiConfig;
import com.yifan.admin.api.enums.AiConfigTypeEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.xunfei.ChatCompletionRequest;
import com.yifan.admin.api.model.xunfei.ChatCompletionResponse;
import com.yifan.admin.api.model.xunfei.Message;
import com.yifan.admin.api.model.xunfei.OutPayload;
import com.yifan.admin.api.service.FunctionService;
import com.yifan.admin.api.tool.FunctionOutput;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import com.yifan.admin.api.webSocket.XunFeiWebSocketListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/11/9
 */
@Slf4j
@Service
public class XunFeiChatServiceImpl implements XunFeiChatService {

    @Resource
    private FunctionService functionService;

    @Override
    public String getName() {
        return AiConfigTypeEnum.CHAT_XUNFEI.getName();
    }

    @Override
    public boolean isSupported(AiConfig aiConfig) {
        return aiConfig != null && StringUtils.equals(aiConfig.getConfigType(), getName());
    }

    @Override
    public void simpleStreamChat(AiConfig aiConfig, String prompt, Consumer<Map<String, Object>> doneCallback, Consumer<String> contentCallback, Consumer<Exception> errorCallback) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(MessageRoles.USER, prompt));
        doChat(aiConfig, messages, resultMap, doneCallback, contentCallback, errorCallback);
    }

    public void doChat(AiConfig aiConfig,
                       List<Message> messages,
                       Map<String, Object> resultMap,
                       Consumer<Map<String, Object>> doneCallback,
                       Consumer<String> contentCallback,
                       Consumer<Exception> errorCallback) {

        final String authConfig = aiConfig.getAuthConfig();
        if (StringUtils.isBlank(authConfig)) {
            log.error("auth config is blank");
            throw new ApiException("service error");
        }

        String[] auths = authConfig.split(":");
        if (auths.length != 4) {
            log.error("auth config length error");
            throw new ApiException("service error");
        }

        SparkDeskClient sparkDeskClient = SparkDeskClient.builder()
                .host(aiConfig.getServerUrl())
                .appid(auths[1])
                .apiKey(auths[2])
                .apiSecret(auths[3])
                .build();

        ChatCompletionRequest request = new ChatCompletionRequest()
                .buildHeader(auths[1])
                .buildParameter(auths[0])
                .buildPayLoad(messages, functionService.getFunctionDefinitions());

        log.info("request params: {}", JacksonJsonUtil.toJson(request));

        sparkDeskClient.chat(new XunFeiWebSocketListener(request) {
            @Override
            public void onWebSocketError(Throwable t, Response response) {
                log.error("error,", t);
                errorCallback.accept((Exception) t);
            }

            @Override
            public void onChatError(ChatCompletionResponse aiChatResponse) {
                log.error("error, resposne: {}.", aiChatResponse);
                errorCallback.accept(new RuntimeException(aiChatResponse.getHeader().getMessage()));
            }

            @Override
            public void onChatOutput(ChatCompletionResponse aiChatResponse) {
                List<Message> message = aiChatResponse.getPayload().getChoices().getText();
                if (!message.isEmpty()) {
                    Message mess = message.get(0);
                    if (mess.getFunctionCall() != null) {
                        log.info("functionCall: {}", JacksonJsonUtil.toJson(mess.getFunctionCall()));
                        FunctionOutput functionOutput = functionService.call(mess.getFunctionCall().getName(), mess.getFunctionCall().getArguments());
                        if (functionOutput != null) {
                            log.info("functionOutput: {}", JacksonJsonUtil.toJson(functionOutput));
                            contentCallback.accept(functionOutput.getResult());
                            contentCallback.accept("\n\n好的，根据你的需求，我为你创作了画作。如果需要继续让我为你作画，请完整描述你的需求。");
                        } else {
                            contentCallback.accept("遇到点问题，稍后再试试吧！");
                        }
                    } else {
                        log.info("reply: {}", mess.getContent());
                        contentCallback.accept(mess.getContent());
                    }
                }
            }

            @Override
            public void onChatEnd() {
                doneCallback.accept(resultMap);
            }

            @Override
            public void onChatToken(OutPayload.Usage usage) {
                log.info("usage: {}.", JacksonJsonUtil.toJson(usage));
            }
        });

    }
}
