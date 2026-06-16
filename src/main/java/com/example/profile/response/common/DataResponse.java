package com.example.profile.response.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Getter
@Setter
public class DataResponse {
    final String appName = "Profile";
    final LocalDate date = LocalDate.now();
    private int status;
    private String code;
    private Object data;
    private String message;

    public DataResponse(Object data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status.value();
    }

    public DataResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
