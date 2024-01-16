package com.yifan.admin.api.model.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/12/6
 */
@Data
public class CreateAiConversationParams {
    @NotNull(message = "参数无效")
    private Long robotId;

    private String name;

}
