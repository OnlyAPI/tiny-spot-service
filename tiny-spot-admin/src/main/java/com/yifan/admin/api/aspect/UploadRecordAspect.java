package com.yifan.admin.api.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.FileUploadRecord;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.service.FileUploadRecordService;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.FileUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/14 10:26
 */
@Aspect
@Component
public class UploadRecordAspect {

    private static final Logger log = LoggerFactory.getLogger(UploadRecordAspect.class);

    @Autowired
    private FileUploadRecordService fileUploadRecordService;

    @Pointcut(value = "@annotation(com.yifan.admin.api.annotition.UploadRecord)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        FileUploadRecord fileUploadRecord = new FileUploadRecord();
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) arg;
                log.info("[around] file size: {}, file name: {}.", file.getSize(), file.getOriginalFilename());

                final String fileHash = FileUtil.getFileHash(file.getInputStream());
                final String fileType = FileUtil.getFileExtensionName(file.getOriginalFilename());

                LambdaQueryWrapper<FileUploadRecord> eq = new LambdaQueryWrapper<FileUploadRecord>()
                        .eq(FileUploadRecord::getFileType, fileType)
                        .eq(FileUploadRecord::getExcerptHash, fileHash);
                FileUploadRecord serviceOne = fileUploadRecordService.getOne(eq);

                if (serviceOne != null) {
                    log.info("[around] upload file exists. hash: {}, type: {}.", fileHash, fileType);
                    return serviceOne.getFileUrl();
                }

                fileUploadRecord.setFileName(file.getOriginalFilename());
                fileUploadRecord.setFileType(fileType);
                fileUploadRecord.setFileSize(FileUtil.getFileFormatSize(file.getSize()));
                fileUploadRecord.setExcerptHash(fileHash);
            } else if (arg instanceof String) {
                fileUploadRecord.setFileName("AI-GENERATE");
                fileUploadRecord.setFileType("png");
            }
        }
        SysUser user = RequestContext.getUser();
        if (user != null) {
            fileUploadRecord.setUploadUserId(user.getId());
        }
        Object proceed = pjp.proceed();
        if (proceed instanceof String) {
            String fileUrl = (String) proceed;
            fileUploadRecord.setFileUrl(fileUrl);
            fileUploadRecord.setCreateTime(DateTimeUtil.currentDate());
            fileUploadRecord.setFileSize(FileUtil.getFileFormatSize(getImageSize(fileUrl)));
            boolean save = fileUploadRecordService.save(fileUploadRecord);
            log.info("[around] save result: {}.", save);
        }
        return proceed;
    }

    private int getImageSize(String imageUrl) throws IOException {
        if (!imageUrl.contains("http")) {
            return 0;
        }
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        int size = connection.getContentLength();
        connection.disconnect();
        return size;
    }
}
