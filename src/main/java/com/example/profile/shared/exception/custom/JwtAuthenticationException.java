package com.example.profile.shared.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
