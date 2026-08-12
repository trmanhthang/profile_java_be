package com.example.profile.modules.idempotencyKey.dto;

import com.example.profile.shared.enums.IdempotencyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IdempotencyRedisDto {
    private IdempotencyStatus status;

    private Integer httpStatus;

    private String response;

    private String requestHash;
}

