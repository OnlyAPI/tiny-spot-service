package com.yifan.admin.api.tool.image;

import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/4 16:44
 */
@Data
public class ImageGenerationInput {

    private String content;

    private String prompt;

    private Integer n;

}
