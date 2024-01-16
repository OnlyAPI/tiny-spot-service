package com.yifan.admin.api.function;

import com.yifan.admin.api.entity.AiConfig;

import java.util.List;
import java.util.function.Consumer;

@FunctionalInterface
public interface ImageGenerationFunctionalInterface<T> {

    void apply(String taskId, AiConfig aiConfig, T params, Consumer<List<String>> resultConsumer, Consumer<Exception> errorCallback);

}
