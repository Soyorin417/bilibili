package com.backend.bilibili.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    // 存字符串
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 取字符串
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除key
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
