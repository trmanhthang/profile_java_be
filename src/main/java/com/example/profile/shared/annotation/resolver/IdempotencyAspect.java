package com.example.profile.shared.annotation.resolver;

import com.example.profile.modules.idempotencyKey.dto.IdempotencyRedisDto;
import com.example.profile.modules.idempotencyKey.entity.IdempotencyKey;
import com.example.profile.modules.idempotencyKey.service.IIdempotencyKeyRedisService;
import com.example.profile.modules.idempotencyKey.service.IIdempotencyKeyService;
import com.example.profile.shared.enums.IdempotencyStatus;
import com.example.profile.shared.exception.custom.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class IdempotencyAspect {

    private final IIdempotencyKeyRedisService redisService;

    private final IIdempotencyKeyService idempotencyKeyService;

    private final ObjectMapper objectMapper;

    @Value("${idempotency.header-name:Idempotency-Key}")
    private String headerName;

    @Around("@annotation(Idempotent)")
    public Object around(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String key = request.getHeader(headerName);
        validateKey(key);

        // Tính hash của request body
        String requestHash = calculateRequestHash(joinPoint);

        boolean acquired = redisService.acquire(key);

        if (acquired) {
            return processFirstRequest(joinPoint, key, requestHash);
        }

        return processDuplicateRequest(key, requestHash);
    }

    private Object processFirstRequest(ProceedingJoinPoint joinPoint, String key, String requestHash) throws Throwable {
        try {
            // Tạo record PROCESSING nếu chưa có
            if (idempotencyKeyService.findByKey(key).isEmpty()) {
                idempotencyKeyService.createProcessing(key, requestHash);
            } else {
                // Key đã tồn tại trong DB nhưng Redis lại acquire được → bất thường
                redisService.delete(key);
                throw new BusinessException(HttpStatus.CONFLICT, "Idempotency key conflict");
            }

            Object result = joinPoint.proceed();

            String responseJson = objectMapper.writeValueAsString(result);
            Integer httpStatus = getHttpStatus(result);

            // Lưu SUCCESS
            idempotencyKeyService.saveSuccess(key, httpStatus, responseJson);

            IdempotencyRedisDto redisDto = IdempotencyRedisDto.builder()
                                                              .status(IdempotencyStatus.SUCCESS)
                                                              .httpStatus(httpStatus)
                                                              .response(responseJson)
                                                              .requestHash(requestHash)
                                                              .build();

            redisService.save(key, objectMapper.writeValueAsString(redisDto), Duration.ofHours(24));

            return result;

        } catch (Throwable ex) {
            // Fail → xóa Redis + đánh dấu FAILED trong DB
            redisService.delete(key);
            idempotencyKeyService.markFailed(key);
            throw ex;
        }
    }

    private Object processDuplicateRequest(String key, String requestHash) throws Exception {

        // 1. Ưu tiên Redis
        String redisValue = redisService.get(key);
        if (redisValue != null) {
            IdempotencyRedisDto dto = objectMapper.readValue(redisValue, IdempotencyRedisDto.class);

            // Kiểm tra requestHash
            if (dto.getRequestHash() != null && !dto.getRequestHash().equals(requestHash)) {
                throw new BusinessException(HttpStatus.CONFLICT, "Idempotency-Key đã được sử dụng với request khác");
            }

            if (dto.getStatus() == IdempotencyStatus.PROCESSING) {
                throw new BusinessException(HttpStatus.CONFLICT, "Request đang được xử lý");
            }

            if (dto.getStatus() == IdempotencyStatus.SUCCESS) {
                return buildResponse(dto.getHttpStatus(), dto.getResponse());
            }
        }

        // 2. Fallback sang DB
        IdempotencyKey entity = idempotencyKeyService.findByKey(key).orElse(null);

        if (entity == null) {
            throw new BusinessException(HttpStatus.CONFLICT, "Request đang được xử lý hoặc key không hợp lệ");
        }

        // Kiểm tra requestHash
        if (!entity.getRequestHash().equals(requestHash)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Idempotency-Key đã được sử dụng với request khác");
        }

        if (entity.getStatus() == IdempotencyStatus.PROCESSING) {
            throw new BusinessException(HttpStatus.CONFLICT, "Request đang được xử lý");
        }

        if (entity.getStatus() == IdempotencyStatus.SUCCESS) {
            // Load lại vào Redis để lần sau nhanh hơn
            IdempotencyRedisDto redisDto = IdempotencyRedisDto.builder()
                                                              .status(IdempotencyStatus.SUCCESS)
                                                              .httpStatus(entity.getHttpStatus())
                                                              .response(entity.getResponse())
                                                              .requestHash(entity.getRequestHash())
                                                              .build();
            redisService.save(key, objectMapper.writeValueAsString(redisDto), Duration.ofHours(24));

            return buildResponse(entity.getHttpStatus(), entity.getResponse());
        }

        throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Idempotency state không hợp lệ");
    }

    private String calculateRequestHash(ProceedingJoinPoint joinPoint) {
        try {
            Object body = Arrays.stream(joinPoint.getArgs())
                                .filter(arg -> !(arg instanceof HttpServletRequest
                                                 || arg instanceof HttpServletResponse
                                                 || arg instanceof BindingResult))
                                .findFirst()
                                .orElse(null);

            String content = body != null ? objectMapper.writeValueAsString(body) : "";
            return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return "unknown";
        }
    }

    private Integer getHttpStatus(
            Object result
    ) {

        if (result instanceof ResponseEntity<?> responseEntity) {

            return responseEntity
                    .getStatusCode()
                    .value();
        }

        return HttpStatus.OK.value();
    }

    private ResponseEntity<?> buildResponse(
            Integer httpStatus,
            String response
    ) throws Exception {

        Object body =
                objectMapper.readValue(
                        response,
                        Object.class
                );

        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }

    private void validateKey(String key) {

        if (key == null || key.isBlank()) {

            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Idempotency-Key không được để trống"
            );
        }

        if (key.length() > 100) {

            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Idempotency-Key không được vượt quá 100 ký tự"
            );
        }
    }
}