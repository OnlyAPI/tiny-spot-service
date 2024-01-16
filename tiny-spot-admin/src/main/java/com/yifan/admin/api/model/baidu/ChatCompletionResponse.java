package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionResponse {

    private String id;

    private String object;

    private Long created;

    private String sentenceId;

    private Boolean isEnd;

    private Boolean isTruncated;

    private String result;

    private Boolean needClearHistory;

    private Integer banRound;

    private Usage usage;

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage {

        private Integer promptTokens;

        private Integer completionTokens;

        private Integer totalTokens;

    }
}
