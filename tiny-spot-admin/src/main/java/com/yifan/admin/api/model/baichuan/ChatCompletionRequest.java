package com.yifan.admin.api.model.baichuan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionRequest {

    private String model;

    private List<Message> messages;

    private Boolean stream;

    private Float temperature = 0.3F;

    private Float topP = 0.85F;

    private Integer topK = 5;

    private Boolean withSearchEnhance = false;

}
