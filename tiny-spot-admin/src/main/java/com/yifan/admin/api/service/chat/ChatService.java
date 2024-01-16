package com.yifan.admin.api.service.chat;

import com.yifan.admin.api.entity.AiConfig;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/7 16:18
 */
public interface ChatService {

    String getName();

    boolean isSupported(AiConfig aiConfig);

    void simpleStreamChat(AiConfig aiConfig, String prompt, Consumer<Map<String, Object>> doneCallback, Consumer<String> contentCallback, Consumer<Exception> errorCallback);

}
