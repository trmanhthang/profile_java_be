package com.example.profile.response.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
}
