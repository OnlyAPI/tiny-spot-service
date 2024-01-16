package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yifan.admin.api.tool.FunctionDefinition;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/9/20 11:55
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionRequest {

    private InHeader header;

    private InParameter parameter;

    private InPayload payload;


    public ChatCompletionRequest buildHeader(String appId) {
        this.header = new InHeader(appId, RandomStringUtils.random(20, true, true));
        return this;
    }

    public ChatCompletionRequest buildParameter(String domain) {
        this.parameter = new InParameter(new InParameter.Chat(domain));
        return this;
    }

    public ChatCompletionRequest buildPayLoad(List<Message> messages) {
        this.payload = new InPayload(new InPayload.InMessage(messages));
        return this;
    }

    public ChatCompletionRequest buildPayLoad(List<Message> messages, List<FunctionDefinition> functionDefinitions) {
        this.payload = new InPayload(new InPayload.InMessage(messages), new InPayload.InFunction(functionDefinitions));
        return this;
    }

}
