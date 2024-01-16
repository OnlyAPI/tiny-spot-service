package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 15:25
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Message {

    private String role;

    private String content;


    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message() {
    }

}
