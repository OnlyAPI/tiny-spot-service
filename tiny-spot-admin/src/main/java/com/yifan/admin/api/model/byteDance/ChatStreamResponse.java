package com.yifan.admin.api.model.byteDance;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatStreamResponse {

    private String reqId;

    private Choice choice;

    private Usage usage;

    private ChatError error;


    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Choice {
        private Message message;

        private String finishReason;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage {

        private Integer promptTokens;

        private Integer completionTokens;

        private Integer totalTokens;

    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatError {
        private String code;
        private Long codeN;
        private String message;
    }
}
