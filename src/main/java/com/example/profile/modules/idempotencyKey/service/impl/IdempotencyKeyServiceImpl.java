package com.example.profile.modules.idempotencyKey.service.impl;

import com.example.profile.modules.idempotencyKey.entity.IdempotencyKey;
import com.example.profile.modules.idempotencyKey.repository.IdempotencyKeyRepository;
import com.example.profile.modules.idempotencyKey.service.IIdempotencyKeyService;
import com.example.profile.shared.enums.IdempotencyStatus;
import com.example.profile.shared.exception.custom.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyKeyServiceImpl implements IIdempotencyKeyService {
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    @Value("${idempotency.ttl-hours:24}")
    private long ttlHours;

    @Override
    public Optional<IdempotencyKey> findByKey(String key) {
        return this.idempotencyKeyRepository.findByIdempotencyKey(key);
    }

    @Override
    @Transactional
    public IdempotencyKey createProcessing(String key, String requestHash) {
        IdempotencyKey object = new IdempotencyKey();

        object.setIdempotencyKey(key);
        object.setRequestHash(requestHash);
        object.setStatus(IdempotencyStatus.PROCESSING);
        object.setExpiresAt(LocalDateTime.now().plusHours(ttlHours));

        return this.idempotencyKeyRepository.save(object);
    }

    @Override
    @Transactional
    public void saveSuccess(String key, Integer httpStatus, String response) {
        IdempotencyKey object = this.idempotencyKeyRepository.findByIdempotencyKey(key).orElseThrow(() -> new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Idempotency key not found"));

        object.setStatus(IdempotencyStatus.SUCCESS);

        object.setHttpStatus(httpStatus);

        object.setResponse(response);

        this.idempotencyKeyRepository.save(object);
    }

    @Override
    public void markFailed(String key) {
        idempotencyKeyRepository.findByIdempotencyKey(key).ifPresent(entity -> {
            entity.setStatus(IdempotencyStatus.FAILED);
            idempotencyKeyRepository.save(entity);
        });
    }
}
