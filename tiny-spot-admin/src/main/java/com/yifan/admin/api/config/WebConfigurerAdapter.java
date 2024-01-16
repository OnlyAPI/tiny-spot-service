package com.yifan.admin.api.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yifan.admin.api.config.properties.IgnoreUrlsProperty;
import com.yifan.admin.api.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Slf4j
@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer {


    @Autowired
    TokenInterceptor tokenInterceptor;

    @Autowired
    private IgnoreUrlsProperty ignoreUrlsConfig;


    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "DELETE", "POST", "GET");
    }

    //josn转换配置
    @Bean
    public HttpMessageConverters customConverters() {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
        builder.serializerByType(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal decimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (decimal == null) {
                    jsonGenerator.writeNull();
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.##################");
                jsonGenerator.writeString(decimalFormat.format(decimal));
            }
        });
        ObjectMapper objectMapper = builder.build();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new NullValueBeanSerializerModifier()));
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        return new HttpMessageConverters(jackson2HttpMessageConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).excludePathPatterns(ignoreUrlsConfig.getUrls()).order(1);
    }

}
