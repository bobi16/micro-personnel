package com.bobiko.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-27
 * @Description: Redis配置类
 */
@Configuration
public class RedisConfig
{
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用Fastjson2序列化器
//        FastJsonRedisSerializer<Object> fastJsonSerializer = new FastJsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) //不为空才序列化
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY) // 可见性，只序列化任意修饰符的字段
                .indentOutput(true) // 美化格式
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 关闭某个特征
                .build();
        // 启用自动包含类型信息，用于反序列化时重建对象的实际类型
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),  //  验证器，用于验证实际要反序列化的子类型是否有效
                ObjectMapper.DefaultTyping.NON_FINAL, // 定义哪些类型的对象需要添加额外的类型信息，NON_FINAL：非 final类都会包含
                JsonTypeInfo.As.PROPERTY); // 类型信息的包含方式 PROPERTY：类型信息作为JSON对象的一个属性

        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        // 设置key的序列化器为StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置value的序列化器为Fastjson2
//        template.setValueSerializer(fastJsonSerializer);
//        template.setHashValueSerializer(fastJsonSerializer);

        // 设置value的序列化器为jackson
        template.setValueSerializer(jacksonSerializer);
        template.setHashValueSerializer(jacksonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
