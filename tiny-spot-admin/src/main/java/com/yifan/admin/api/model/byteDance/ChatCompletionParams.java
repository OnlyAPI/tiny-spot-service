package com.yifan.admin.api.model.byteDance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionParams {

    private Model model;

    private List<Message> messages;

    private Boolean stream;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Model {
        private String name = SkylarkModels.SKYLARK_CHAT;

    }


    public static class SkylarkModels {
        public static final String SKYLARK_LITE_PUBLIC = "skylark-lite-public";
        public static final String SKYLARK_PLUS_PUBLIC = "skylark-plus-public";
        public static final String SKYLARK_PRO_PUBLIC = "skylark-pro-public";
        public static final String SKYLARK_CHAT = "skylark-chat";
    }

}
