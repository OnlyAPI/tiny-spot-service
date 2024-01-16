package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yifan.admin.api.enums.ImageGenerateSizeEnum;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 18:05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Txt2ImgRequest {

    /**
     * 提示词
     */
    private String prompt;

    /**
     * "768x768", "768x1024", "1024x768", "576x1024", "1024x576", "1024x1024"
     */
    private String size = ImageGenerateSizeEnum.SIZE_1024_1024.getSize();

    /**
     * 生成的图片数量
     */
    private Integer n;


}
