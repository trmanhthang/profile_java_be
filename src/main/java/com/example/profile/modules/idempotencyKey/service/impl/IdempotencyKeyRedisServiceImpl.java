package com.example.profile.modules.idempotencyKey.service.impl;

import com.example.profile.modules.idempotencyKey.service.IIdempotencyKeyRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyKeyRedisServiceImpl implements IIdempotencyKeyRedisService {
    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "idempotency:";

    @Override
    public boolean acquire(String key) {

        Boolean result = redisTemplate.opsForValue()
                                      .setIfAbsent(
                                              this.buildKey(key),
                                              "PROCESSING",
                                              Duration.ofMinutes(5)
                                      );
        return Boolean.TRUE.equals(result);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue()
                            .get(this.buildKey(key));
    }

    @Override
    public void save(String key, String value, Duration duration) {
        this.redisTemplate.opsForValue()
                          .set(
                                  this.buildKey(key),
                                  value,
                                  duration
                          );
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(this.buildKey(key));
    }

    private String buildKey(String key) {

        return PREFIX + key;
    }
}
