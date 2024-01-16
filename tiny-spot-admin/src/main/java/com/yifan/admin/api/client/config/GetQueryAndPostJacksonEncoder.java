package com.yifan.admin.api.client.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

@Slf4j
public class GetQueryAndPostJacksonEncoder extends SpringFormEncoder {

    private final ObjectMapper mapper;

    public GetQueryAndPostJacksonEncoder() {
        this(new ObjectMapper());
    }

    public GetQueryAndPostJacksonEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {
        if (bodyType.equals(MultipartFile.class)) {
            val file = (MultipartFile) object;
            val data = singletonMap(file.getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
        } else {
            if (Request.HttpMethod.GET.name().equalsIgnoreCase(template.method())) {
                Map<String, Collection<String>> queryMap = new HashMap<>();

                ReflectionUtils.doWithMethods(object.getClass(), method -> {
                    final String methodName = method.getName();
                    if (!methodName.startsWith("get") || methodName.equals("getClass")) {
                        return;
                    }

                    Object value = null;
                    try {
                        value = method.invoke(object);
                    } catch (Exception e) {
                        log.error("[calcSignAndSet] method invoke error. class: {}, method: {}",
                                this.getClass(), methodName, e);
                    }

                    if (value == null || StringUtils.isBlank(value.toString())) {
                        return;
                    }

                    String fieldName = methodName.substring(3);
                    fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

                    if (value instanceof Collection) {
                        Set<String> valueList = new ArrayList<>((Collection<?>) value).stream()
                                .map(Object::toString).collect(Collectors.toSet());
                        queryMap.put(fieldName, valueList);
                    } else {
                        queryMap.put(fieldName, Collections.singleton(value.toString()));
                    }
                });
                template.queries(queryMap);
            } else {
                try {
                    JavaType javaType = mapper.getTypeFactory().constructType(bodyType);
                    template.body(mapper.writerFor(javaType).writeValueAsBytes(object), Util.UTF_8);
                } catch (JsonProcessingException e) {
                    throw new EncodeException(e.getMessage(), e);
                }
            }
        }
    }

}
