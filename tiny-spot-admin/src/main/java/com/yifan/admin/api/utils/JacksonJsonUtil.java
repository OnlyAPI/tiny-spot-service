package com.yifan.admin.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 18:19
 */
public class JacksonJsonUtil {

    private static volatile ObjectMapper objectMapper;
    private static final Object o = new Object();

    private JacksonJsonUtil() {
    }

    public static <T> String toJson(T object) {
        try {
            return object == null ? "" : getObjectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException("service error");
        }
    }

    public static <T> T fromJson(String json, Class<T> typeOfObject) {
        try {
            return StringUtils.isBlank(json) ? null : getObjectMapper().readValue(json, typeOfObject);
        } catch (Exception ex) {
            throw new RuntimeException("service error");
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return StringUtils.isBlank(json) ? null : getObjectMapper().readValue(json, typeReference);
        } catch (Exception ex) {
            throw new RuntimeException("service error");
        }
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized(o) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }

        return objectMapper;
    }
}
