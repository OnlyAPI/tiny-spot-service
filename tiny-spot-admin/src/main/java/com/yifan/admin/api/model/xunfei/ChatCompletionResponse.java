package com.yifan.admin.api.model.xunfei;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/9/20 14:03
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionResponse {
    private OutHeader header;

    private OutPayload payload;

}
