package com.example.profile.shared.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ApiResponse<T> {
    final String appName = "Profile";
    final LocalDate date = LocalDate.now();
    private int status;
    private T result;
    private String message;

    public static <T> ResponseEntity<ApiResponse<T>> custom(T payload, String message, HttpStatus status) {
        ApiResponse<T> data = ApiResponse.<T>builder()
                .status(status.value())
                .message(message)
                .result(payload)
                .build();
        return ResponseEntity.status(status).body(data);
    }
}
