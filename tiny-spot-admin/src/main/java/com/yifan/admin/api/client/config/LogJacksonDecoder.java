package com.yifan.admin.api.client.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class LogJacksonDecoder extends JacksonDecoder {

    private final ObjectMapper objectMapper;

    public LogJacksonDecoder() {
        this(new ObjectMapper());
    }

    public LogJacksonDecoder(ObjectMapper mapper) {
        super(mapper);
        this.objectMapper = mapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Object data = super.decode(response, type);
        try {
            log.info("[response] data: {}", objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.error("[response] write json string error.", e);
        }
        return data;
    }

}
