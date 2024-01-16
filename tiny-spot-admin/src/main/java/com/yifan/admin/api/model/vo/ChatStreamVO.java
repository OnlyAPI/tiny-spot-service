package com.yifan.admin.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/9/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatStreamVO {

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已完成
     */
    private Boolean finished;

    /**
     * 完成原因
     */
    private String finishReason;

}
