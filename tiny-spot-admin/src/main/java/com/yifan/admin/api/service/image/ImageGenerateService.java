package com.yifan.admin.api.service.image;

import com.yifan.admin.api.entity.AiConfig;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 17:54
 */
public interface ImageGenerateService<T> {

    void asyncTxt2img(String taskId, AiConfig aiConfig, T t, Consumer<List<String>> resultConsumer, Consumer<Exception> errorCallback);

    List<String> imgBaseVal2Url(List<String> imgBase64List);
}
