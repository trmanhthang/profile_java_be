package com.example.profile.shared.exception;

import com.example.profile.response.common.ErrorResponse;
import com.example.profile.shared.constant.ErrorMessageConstant;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandling {


    /**
     * sai mật khẩu người dùng
     */
    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<?> badCredentialsException(BadCredentialsException ex) {
        log.error("Exception wrong password: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    /**
     * không tìm thấy username người dùng trong database
     *
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<?> internalAuthenticationException(UsernameNotFoundException ex) {
        log.error("Exception not found by username in database: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());

        String message = ex.getMessage();
        message = StringUtils.isNotBlank(message) && message.length() > 17 && message.contains("400 BAD_REQUEST") ? message.substring(17, message.length() - 1) : message;
        return ErrorResponse.build(ex.getMessage(), message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> BindRequestException(BindException ex) {
        log.error("Exception error data input: {}", ex.getMessage());
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(fieldErrorList, ErrorMessageConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * các thiết tham số truyền vào controller trong truy vấn ở @RequestParem
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MidssingParameter(MissingServletRequestParameterException ex) {
        log.error("Exception missing parameter data input: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.ERROR_INPUT_PARAMETER, HttpStatus.BAD_REQUEST);
    }

    /**
     * tham số truyền vào không hợp lệ ở controller
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Exception error invalid input: ");
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        return ErrorResponse.build(fieldErrorList, ErrorMessageConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * vượt quá kích thước file
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> maxFileExceeded(MaxUploadSizeExceededException ex) {
        log.error("Exception Max file: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.ERROR_MAX_FILE, HttpStatus.BAD_REQUEST);
    }

    /**
     * lỗi liên quan đến độ dài trong sql
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> SQLValidException(DataIntegrityViolationException ex) {
        log.error("Exception SQL length data: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.ERROR_INPUT_SQL, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * method not support
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> notSupportMethodException(HttpRequestMethodNotSupportedException ex) {
        log.error("Exception not support method: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.NOT_SUPPORT_METHOD, HttpStatus.BAD_REQUEST);
    }

    /**
     * các Exception trong nghiệp vụ hệ thống
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> BadRequestException(ResponseStatusException ex) {
        log.error("Exception business: {}", ex.getReason());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ex.getReason(), HttpStatus.BAD_REQUEST);
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> NoSuchException(NoSuchElementException ex) {
        log.error("Exception not found in database no such: {}",  ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_FIND_NO_SUCH, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * không tồn tại trong database khi thực hiện xóa
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> EmptyRequestException(EmptyResultDataAccessException ex) {
        log.error("Exception not found in database when delete: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_DELETE, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm với các loại tìm kiếm cũ
     */
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<?> NotFoundException(ChangeSetPersister.NotFoundException ex) {
        log.error("Exception not found in database: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_FIND, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * tìm thấy nhiều hơn một row trong database
     */
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<?> nonUniqueResultException(IncorrectResultSizeDataAccessException ex) {
        log.error("Exception non unique result: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.NON_UNIQUE_RESULT, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * lỗi null poiter
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullPointerException(NullPointerException ex) {
        log.error("Exception null pointer: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ErrorResponse.build("Exception null pointer: " + ex.getMessage(), ErrorMessageConstant.ERROR_COMMON_NULL_POINT, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * lỗi cú pháp sql
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<?> SQLException(InvalidDataAccessResourceUsageException ex) {
        log.error("Exception SQL syntax: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.ERROR_COMMON_SQL, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * bắt tất cả các exception còn lại không kiểm soát được trong hệ thống
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> processIOException(Exception ex) {
        log.error("Exception error code: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ErrorResponse.build(ex.getMessage(), ErrorMessageConstant.ERROR_SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR);
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

    private void showAllLogException(StackTraceElement[] stackTraceElements) {
        for (StackTraceElement line : stackTraceElements) {
            System.out.println(line);
        }
    }
}
