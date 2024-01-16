package com.yifan.admin.api.service.storage;

import com.yifan.admin.api.model.dto.FileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储接口
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 15:05
 */
public interface FileStorageService {

    String fileUpload(MultipartFile file);

    String fileUpload(String imgBase64);

    String fileUpload(FileUploadDTO param);
}
