package com.yifan.admin.api.controller;

import com.yifan.admin.api.service.storage.FileStorageService;
import com.yifan.admin.api.result.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/15 10:19
 */
@RequestMapping("/file")
@RestController
@Api(tags = "文件上传管理")
@Slf4j
public class FileUploadController {

    @Autowired
    FileStorageService fileStorageService;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public BaseResult<String> fileUpload(@RequestPart(value = "file") MultipartFile file) {
        log.info("[fileUpload] file size: {}.", file.getSize());
        return BaseResult.ok(fileStorageService.fileUpload(file));
    }

}
