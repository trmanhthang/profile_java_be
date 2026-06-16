package com.example.profile.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandling {


    /**
     * sai mật khẩu người dùng
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<?> badCredentialsException(BadCredentialsException ex) {
        log.error("Exception wrong password: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    private void showLogException(StackTraceElement[] stackTraceElements) {
        int i = 1;
        for (StackTraceElement line : stackTraceElements) {
            System.out.println(line);
            if (i == 2) {
                break;
            }
            i++;
        }
    }
}
