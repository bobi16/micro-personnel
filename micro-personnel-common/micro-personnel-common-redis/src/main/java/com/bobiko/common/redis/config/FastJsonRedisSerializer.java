package com.bobiko.common.redis.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.bobiko.common.core.constants.RedisConstants;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-27
 * @Description: redis序列化器
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>
{
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(RedisConstants.JSON_WHITELIST_STR);

    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (null == value){
            return new byte[0];
        }
        return JSON.toJSONString(value, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String json = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(json, clazz, AUTO_TYPE_FILTER);
    }
}
