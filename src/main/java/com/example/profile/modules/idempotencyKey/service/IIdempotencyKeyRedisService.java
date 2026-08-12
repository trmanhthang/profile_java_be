package com.example.profile.modules.idempotencyKey.service;

import java.time.Duration;

public interface IIdempotencyKeyRedisService {
    boolean acquire(String key);

    String get(String key);

    void save(String key, String value, Duration duration);

    void delete(String key);
}
