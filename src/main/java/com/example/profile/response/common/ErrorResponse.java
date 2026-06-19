package com.example.profile.response.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponse {
    public static ResponseEntity<?> build(Object data, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new DataResponse(data, message, httpStatus), httpStatus);
    }
}