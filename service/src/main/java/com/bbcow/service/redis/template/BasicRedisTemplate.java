package com.bbcow.service.redis.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class BasicRedisTemplate<V> extends RedisTemplate<String, V> {
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    public BasicRedisTemplate(Class<V> v){
        setKeySerializer(new StringRedisSerializer());
        setValueSerializer(new Jackson2JsonRedisSerializer<>(v));
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return redisConnectionFactory;
    }
}
