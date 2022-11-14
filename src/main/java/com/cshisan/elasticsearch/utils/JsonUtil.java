package com.cshisan.elasticsearch.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * @author yuanbai
 */
public class JsonUtil {
    private static final ObjectMapper SIMPLE_MAPPER = new ObjectMapper();
    private static final ObjectMapper SERIALIZE_MAPPER = new ObjectMapper();

    static {
        // 忽略不存在值
        SIMPLE_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SERIALIZE_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化配置
        SERIALIZE_MAPPER.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    }

    /**
     * entity转string
     *
     * @see JsonUtil#toJson(Object, boolean)
     */
    public static String toJson(Object o) {
        return toJson(o, true);
    }

    /**
     * entity转string
     *
     * @param o        o
     * @param isSimple isSimple
     * @return string
     */
    public static String toJson(Object o, boolean isSimple) {
        try {
            return mapper(isSimple).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON工具转换异常");
        }
    }

    /**
     * json转entity
     *
     * @see JsonUtil#toEntity(String, Class, boolean)
     */
    public static <T> T toEntity(String json, Class<T> clazz) {
        return toEntity(json, clazz, true);
    }

    /**
     * json转entity
     *
     * @param json     json
     * @param clazz    clazz
     * @param isSimple isSimple
     * @return t
     */
    public static <T> T toEntity(String json, Class<T> clazz, boolean isSimple) {
        try {
            return mapper(isSimple).readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON工具转换异常");
        }
    }

    /**
     * entity转entity
     *
     * @param entity entity
     * @param clazz  clazz
     * @return t
     */
    public static <T> T e2e(Object entity, Class<T> clazz) {
        return e2e(entity, clazz, true);
    }

    /**
     * entity转entity
     *
     * @param entity   entity
     * @param clazz    clazz
     * @param isSimple isSimple
     * @return t
     */
    public static <T> T e2e(Object entity, Class<T> clazz, boolean isSimple) {
        return toEntity(toJson(entity, isSimple), clazz, isSimple);
    }

    /**
     * 获取单个属性值
     *
     * @param json      json
     * @param fieldName fieldName
     * @return string
     */
    public static String getField(String json, String fieldName) {
        return getField(json, fieldName, true);
    }

    /**
     * 获取单个属性值
     *
     * @param json      json
     * @param fieldName fieldName
     * @param isSimple  isSimple
     * @return string
     */
    public static String getField(String json, String fieldName, boolean isSimple) {
        try {
            JsonNode tree = mapper(isSimple).readTree(json);
            JsonNode node = tree.get(fieldName);
            return node.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取mapper
     *
     * @param isSimple isSimple
     * @return mapper
     */
    private static ObjectMapper mapper(boolean isSimple) {
        return isSimple ? SIMPLE_MAPPER : SERIALIZE_MAPPER;
    }
}
