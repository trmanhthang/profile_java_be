package com.example.profile.shared.exception;

import com.example.profile.shared.common.ApiResponse;
import com.example.profile.shared.constant.ErrorMessageConstant;
import com.example.profile.shared.exception.custom.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.*;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandling {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<List<FieldError>>> BindRequestException(BindException ex) {
        log.error("Exception error data input: {}", ex.getMessage());
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(fieldErrorList, ErrorMessageConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * các thiết tham số truyền vào controller trong truy vấn ở @RequestParem
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> MissingParameter(MissingServletRequestParameterException ex) {
        log.error("Exception missing parameter data input: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.ERROR_INPUT_PARAMETER, HttpStatus.BAD_REQUEST);
    }

    /**
     * tham số truyền vào không hợp lệ ở controller
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldError>>> ArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Exception error invalid input: ");
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        return ApiResponse.custom(fieldErrorList, ErrorMessageConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * vượt quá kích thước file
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> maxFileExceeded(MaxUploadSizeExceededException ex) {
        log.error("Exception Max file: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.ERROR_MAX_FILE, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    /**
     * lỗi liên quan đến độ dài trong sql
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> SQLValidException(DataIntegrityViolationException ex) {
        log.error("Exception SQL length data: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.ERROR_INPUT_SQL, HttpStatus.CONFLICT);
    }

    /**
     * method not support
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> notSupportMethodException(HttpRequestMethodNotSupportedException ex) {
        log.error("Exception not support method: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.NOT_SUPPORT_METHOD, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * các Exception trong nghiệp vụ hệ thống
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> responseStatusException(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        return ApiResponse.custom(
                ex.getMessage(),
                ex.getReason(),
                status
        );
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> NoSuchException(NoSuchElementException ex) {
        log.error("Exception not found in database no such: {}",  ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_FIND_NO_SUCH, HttpStatus.NOT_FOUND);
    }

    /**
     * không tồn tại trong database khi thực hiện xóa
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<String>> EmptyRequestException(EmptyResultDataAccessException ex) {
        log.error("Exception not found in database when delete: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_DELETE, HttpStatus.NOT_FOUND);
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm với các loại tìm kiếm cũ
     */
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> NotFoundException(ChangeSetPersister.NotFoundException ex) {
        log.error("Exception not found in database: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.NOT_FOUND_DATABASE_FIND, HttpStatus.NOT_FOUND);
    }

    /**
     * tìm thấy nhiều hơn một row trong database
     */
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ApiResponse<String>> nonUniqueResultException(IncorrectResultSizeDataAccessException ex) {
        log.error("Exception non unique result: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.NON_UNIQUE_RESULT, HttpStatus.CONFLICT);
    }

    /**
     * lỗi cú pháp sql
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ApiResponse<String>> SQLException(InvalidDataAccessResourceUsageException ex) {
        log.error("Exception SQL syntax: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.ERROR_COMMON_SQL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * bắt tất cả các exception còn lại không kiểm soát được trong hệ thống
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResponse<String>> processIOException(Exception ex) {
        log.error("Exception error code: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ApiResponse.custom(ex.getMessage(), ErrorMessageConstant.ERROR_SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> authenticationException(
            AuthenticationException ex) {

        log.warn("Authentication failed: {}", ex.getMessage());

        return ApiResponse.custom(
                null,
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> accessDeniedException(AccessDeniedException ex) {

        log.warn("Access denied", ex);

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ACCESS_DENIED,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> constraintViolationException(
            ConstraintViolationException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ERROR_INPUT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> httpMessageNotReadableException(
            HttpMessageNotReadableException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ERROR_INPUT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ERROR_INPUT_PARAMETER,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> httpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                "Content-Type không được hỗ trợ",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<String>> missingRequestHeaderException(
            MissingRequestHeaderException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                "Thiếu Request Header",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> illegalArgumentException(
            IllegalArgumentException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ERROR_INPUT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<String>> optimisticLockingFailureException(
            OptimisticLockingFailureException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                "Dữ liệu đã được cập nhật bởi người khác",
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> businessException(
            BusinessException ex) {

        return ApiResponse.custom(
                ex.getMessage(),
                ErrorMessageConstant.ERROR_BUSINESS,
                ex.getStatus()
        );
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
