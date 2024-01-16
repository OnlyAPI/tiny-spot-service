package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/9/20 18:53
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Message {

    private String role;

    private String content;

    private FunctionCall functionCall;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message() {
    }
}
