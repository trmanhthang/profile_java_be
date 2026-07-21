package com.example.profile.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Duration ttl) {
        this.redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        return clazz.cast(value);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    public Long expire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
        return redisTemplate.getExpire(key);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
}
