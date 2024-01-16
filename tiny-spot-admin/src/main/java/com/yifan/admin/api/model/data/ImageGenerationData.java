package com.yifan.admin.api.model.data;

import lombok.Data;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/21 14:32
 */
@Data
public class ImageGenerationData {

    private String taskId;

    private Boolean success;

    private List<String> imageBase64s;

    private List<String> imageUrls;

    private String errMsg;

}
