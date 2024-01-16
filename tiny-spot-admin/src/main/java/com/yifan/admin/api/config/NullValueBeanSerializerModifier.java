package com.yifan.admin.api.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.List;

public class NullValueBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        BeanPropertyWriter writer;
        JavaType javaType;
        for (int i = 0; i < beanProperties.size(); i++) {
            writer = beanProperties.get(i);
            javaType = writer.getType();
            if (javaType.isArrayType() || javaType.isCollectionLikeType()) {
                //给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeStartArray();
                            gen.writeEndArray();
                        } else {
                            gen.writeObject(value);
                        }
                    }
                });
            } else if (javaType.isMapLikeType()) {
                writer.assignNullSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeStartObject();
                            gen.writeEndObject();
                        } else {
                            gen.writeObject(value);
                        }
                    }
                });
            } else if (javaType.isTypeOrSubTypeOf(Boolean.class)) {
                writer.assignNullSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeObject(false);
                        } else {
                            gen.writeObject(value);
                        }
                    }
                });
            } else if (javaType.isTypeOrSubTypeOf(Number.class)) {
                writer.assignNullSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeObject(0);
                        } else {
                            gen.writeObject(value);
                        }
                    }
                });
            } else if (javaType.isTypeOrSubTypeOf(String.class)) {
                writer.assignNullSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        if (value == null) {
                            gen.writeObject("");
                        } else {
                            gen.writeObject(value);
                        }
                    }
                });
            }
        }
        return beanProperties;
    }
}
