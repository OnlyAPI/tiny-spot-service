package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 15:32
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionRequest {

    private List<Message> messages;

    private Boolean stream;

    /**
     * 模型人设，主要用于人设设定，例如，你是xxx公司制作的AI助手，说明：
     * （1）长度限制1024个字符
     * （2）如果使用functions参数，不支持设定人设system
     */
    private String system;

    private String userId;

}
