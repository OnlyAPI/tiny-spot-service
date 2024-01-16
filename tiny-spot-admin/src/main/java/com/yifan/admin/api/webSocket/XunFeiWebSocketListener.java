package com.yifan.admin.api.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yifan.admin.api.model.xunfei.ChatCompletionRequest;
import com.yifan.admin.api.model.xunfei.ChatCompletionResponse;
import com.yifan.admin.api.model.xunfei.OutPayload;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/9/19
 */
@Getter
@Slf4j
public abstract class XunFeiWebSocketListener extends WebSocketListener {

    /**
     * 请求大模型的参数
     */
    private final ChatCompletionRequest chatCompletionRequest;

    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * 构造星火大模型请求参数，默认使用构造方法传入的信息
     * 可以覆盖重写
     *
     * @return 大模型请求参数
     */
    public ChatCompletionRequest onChatSend() {
        return this.chatCompletionRequest;
    }


    /**
     * 构造方法，传入大模型参数
     *
     * @param request 大模型参数
     */
    public XunFeiWebSocketListener(ChatCompletionRequest request) {
        this.chatCompletionRequest = request;
    }

    /**
     * WebSocket服务发生异常的回调，可以覆盖重写。
     * 默认抛出异常
     *
     * @param t        异常
     * @param response 返回值
     */
    public abstract void onWebSocketError(Throwable t, Response response);

    /**
     * 星火大模型发生异常
     *
     * @param aiChatResponse 大模型返回值
     */
    public abstract void onChatError(ChatCompletionResponse aiChatResponse);

    /**
     * 星火大模型正常返回信息
     *
     * @param aiChatResponse 大模型返回值
     */
    public abstract void onChatOutput(ChatCompletionResponse aiChatResponse);

    /**
     * 星火大模型返回信息结束回调
     */
    public abstract void onChatEnd();

    /**
     * 星火大模型本次请求消耗的Token信息
     *
     * @param usage 大模型返回token信息
     */
    public abstract void onChatToken(OutPayload.Usage usage);


    @SneakyThrows
    @Override
    public final void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        ChatCompletionRequest aiChatRequest = this.onChatSend();
        webSocket.send(mapper.writeValueAsString(Objects.isNull(aiChatRequest) ? this.getChatCompletionRequest() : aiChatRequest));
    }


    @Override
    public final void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        ChatCompletionResponse aiChatResponse = JacksonJsonUtil.fromJson(text, ChatCompletionResponse.class);
        log.info("onMessage: {}.", JacksonJsonUtil.toJson(aiChatResponse));

        if (!aiChatResponse.getHeader().isSuccess()) {
            log.warn("调用星火模型发生错误，错误码为：{}，请求的sid为：{}", aiChatResponse.getHeader().getCode(), aiChatResponse.getHeader().getSid());
            webSocket.close(1000, "星火模型调用异常");
            this.onChatError(aiChatResponse);
            return;
        }

        this.onChatOutput(aiChatResponse);

        if (aiChatResponse.getHeader().isEnd()) {
            // 可以关闭连接，释放资源
            webSocket.close(1000, "星火模型返回结束");
            OutPayload.Usage usage = aiChatResponse.getPayload().getUsage();
            this.onChatEnd();
            this.onChatToken(usage);
        }
    }


    @Override
    public final void onFailure(WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        webSocket.close(1000, "");
        this.onWebSocketError(t, response);
    }

}

