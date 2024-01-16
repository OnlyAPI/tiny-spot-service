package com.yifan.admin.api.model.baichuan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionResponse {
    private String id;
    private Integer created;
    private List<Choices> choices;
    private String model;
    private String object;

    @Data
    public static class Choices {

        @JsonProperty("finish_reason")
        private String finishReason;

        private Integer index;

        private Message message;
    }
}
