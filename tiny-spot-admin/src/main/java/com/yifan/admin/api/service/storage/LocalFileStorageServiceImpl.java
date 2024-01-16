package com.yifan.admin.api.service.storage;

import com.yifan.admin.api.annotition.UploadRecord;
import com.yifan.admin.api.config.properties.OssStorageProperty;
import com.yifan.admin.api.model.dto.FileUploadDTO;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/12 15:54
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "oss", name = "type", havingValue = "local")
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final OssStorageProperty.Local localConfig;

    public LocalFileStorageServiceImpl(OssStorageProperty property) {
        this.localConfig = property.getLocal();
    }

    @Override
    @UploadRecord
    public String fileUpload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        Objects.requireNonNull(originalFileName);
        originalFileName = originalFileName.replaceAll(" ", "");

        final String formatName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        try {
            return fileUpload(formatName, file.getInputStream(), file.getSize());
        } catch (IOException e) {
            log.error("error", e);
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
        } finally {
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
        final String remoteFileName = UuidUtils.randomUUID() + "." + formatName;

        String[] split = DateTimeUtil.currentYMD().split("-");
        String dataStr = String.join("/", split);
        String fileSuffixUrl = String.join("/", dataStr, remoteFileName);

        // web服务器存放的绝对路径
        String absolutePath = Paths.get(localConfig.getPath(), split).toString();
        File outFile = new File(Paths.get(absolutePath, remoteFileName).toString());

        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            FileUtils.writeByteArrayToFile(outFile, outStream.toByteArray());
        } catch (IOException e) {
            log.error("error", e);
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        final String url = localConfig.getPrefix() + "/" + fileSuffixUrl;
        log.info("url: {}", url);
        // 替换掉windows环境的\路径
        return url.replace("\\\\", "/").replace("\\", "/");

    }
}
