package com.yifan.admin.api.model.dto;

import lombok.Data;

import java.io.InputStream;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/13 10:05
 */
@Data
public class FileUploadDTO {

    private String formatName;

    private InputStream inputStream;

    private Long size;
}
