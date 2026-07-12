package com.example.profile.shared.exception.custom;

import com.example.profile.shared.constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException() {
        super(HttpStatus.CONFLICT, ErrorMessageConstant.USERNAME_ALREADY_EXISTS);
    }
}
