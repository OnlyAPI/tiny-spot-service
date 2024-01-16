package com.yifan.admin.api.model.aliyun;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 18:00
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionResponse {

    private String requestId;

    private OutPut output;

    private Usage usage;

    private String code;

    private String message;

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OutPut{
        private String text;

        private String finishReason;

        private List<Choise> choise;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage{
        private Integer outputTokens;

        private Integer inputTokens;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Choise{
        private String finishReason;

        private Message message;

    }

}
