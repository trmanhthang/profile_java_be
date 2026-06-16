package com.example.profile.response.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponse {
    public static ResponseEntity<?> build(Object data, String message) {
        DataResponse response = new DataResponse(data, message, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<?> setMessage(String message) {
        DataResponse response = new DataResponse(message, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
