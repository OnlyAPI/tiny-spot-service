package com.yifan.admin.api.model.baidu;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;


/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 18:28
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Txt2ImgResponse {

    private String id;

    private String object;

    private Long created;

    private List<ImageData> data;

    private Usage usage;

    private Integer errorCode;

    private String errorMsg;

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageData{
        private String object;

        private String b64Image;

        private Integer index;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage{

        private Integer promptTokens;

        private Integer totalTokens;

    }
}
