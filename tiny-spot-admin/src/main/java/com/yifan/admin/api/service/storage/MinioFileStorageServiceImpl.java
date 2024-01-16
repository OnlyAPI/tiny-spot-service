package com.yifan.admin.api.service.storage;

import com.yifan.admin.api.annotition.UploadRecord;
import com.yifan.admin.api.config.properties.OssStorageProperty;
import com.yifan.admin.api.model.dto.FileUploadDTO;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.FileUtil;
import com.yifan.admin.api.utils.UuidUtils;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 15:05
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "oss", name = "type", havingValue = "minio")
public class MinioFileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;

    private final OssStorageProperty.Minio minioConfig;

    public MinioFileStorageServiceImpl(OssStorageProperty property) {
        this.minioConfig = property.getMinio();
        this.minioClient = MinioClient.builder()
                .endpoint(property.getMinio().getEndPoint())
                .credentials(property.getMinio().getAccessKey(), property.getMinio().getSecretKey())
                .build();
    }


    @Override
    @UploadRecord
    public String fileUpload(MultipartFile file) {
        try {
            String formatName = FileUtil.getFileExtensionName(file.getOriginalFilename());
            return fileUpload(formatName, file.getInputStream(), file.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    @UploadRecord
    public String fileUpload(String imgBase64) {
        ByteArrayInputStream bis = null;
        try {
            byte[] imageBytes = Base64.getDecoder().decode(imgBase64);
            bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);
            return fileUpload("png", image);
        } catch (Exception e) {
            log.error("error", e);
        }finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
        return "";
    }

    @Override
    @UploadRecord
    public String fileUpload(FileUploadDTO param) {
        return fileUpload(param.getFormatName(), param.getInputStream(), param.getSize());
    }


    private String fileUpload(String formatName, BufferedImage bufferedImage) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, formatName, byteArrayOutputStream);
            inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            return fileUpload(formatName, inputStream, byteArrayOutputStream.size());
        } catch (IOException e) {
            log.error("[fileUpload] error.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    private String fileUpload(String formatName, InputStream in, long size) {
        try {
            String remoteFileName = UuidUtils.randomUUID() + "." + formatName;
            String[] split = DateTimeUtil.currentYMD().split("-");
            String dataStr = String.join("/", split);
            String fileUrl = String.join("/", dataStr, remoteFileName);

            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());
            }

            ObjectWriteResponse uploadResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())//桶名称
                            .object(fileUrl)//远程文件
                            .stream(in, size, -1)//源文件
                            .build());
            log.info("[uploadFile] upload file etag: {}, versionId: {}.", uploadResponse.etag(), uploadResponse.versionId());
            String remoteFileUrl = String.join("/", minioConfig.getEndPoint(), minioConfig.getBucket(), fileUrl);
            log.info("[uploadFile] upload success. url: {}", remoteFileUrl);

            return remoteFileUrl;
        } catch (Exception e) {
            log.error("[fileUpload] error.", e);
        }
        return "";
    }
}
