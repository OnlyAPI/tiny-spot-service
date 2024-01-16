package com.yifan.admin.api.model.aliyun;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 17:38
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {

    private String model;

    private Input input;

    private Parameters parameters;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Input {
        private List<Message> messages;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Parameters {

        private String resultFormat = "text";

        private Boolean incrementalOutput = true;

    }
}
