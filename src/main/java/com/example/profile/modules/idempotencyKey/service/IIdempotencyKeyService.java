package com.example.profile.modules.idempotencyKey.service;

import com.example.profile.modules.idempotencyKey.entity.IdempotencyKey;

import java.util.Optional;

public interface IIdempotencyKeyService {
    Optional<IdempotencyKey> findByKey(String key);

    IdempotencyKey createProcessing(String key, String requestHash);

    void saveSuccess(String key, Integer httpStatus, String response);

    void markFailed(String key);
}
