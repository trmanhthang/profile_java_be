package com.example.profile.shared.exception.custom;

import com.example.profile.shared.constant.AuthenticationMessageConstant;
import com.example.profile.shared.constant.ErrorMessageConstant;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        super(AuthenticationMessageConstant.USER_NOT_FOUND);
    }
}
