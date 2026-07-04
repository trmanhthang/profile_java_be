package com.example.profile.shared.exception;

import com.example.profile.response.common.ApiResponse;
import com.example.profile.shared.constant.ErrorMessageConstant;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
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
    private ApiResponse<String> badCredentialsException(BadCredentialsException ex) {
        log.error("Exception wrong password: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessageConstant.BAD_CREDENTIALS)
                .result(ex.getMessage())
                .build();
    }

    /**
     * không tìm thấy username người dùng trong database
     *
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    private ApiResponse<String> internalAuthenticationException(UsernameNotFoundException ex) {
        log.error("Exception not found by username in database: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());

        String message = ex.getMessage();
        message = StringUtils.isNotBlank(message) && message.length() > 17 && message.contains("400 BAD_REQUEST") ? message.substring(17, message.length() - 1) : message;
        return ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .result(ex.getMessage())
                .build();
    }

    @ExceptionHandler(BindException.class)
    public ApiResponse<List<FieldError>> BindRequestException(BindException ex) {
        log.error("Exception error data input: {}", ex.getMessage());
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<List<FieldError>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessageConstant.ERROR_INPUT)
                .result(fieldErrorList)
                .build();}

    /**
     * các thiết tham số truyền vào controller trong truy vấn ở @RequestParem
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<String> MissingParameter(MissingServletRequestParameterException ex) {
        log.error("Exception missing parameter data input: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessageConstant.ERROR_INPUT_PARAMETER)
                .result(ex.getMessage())
                .build();
    }

    /**
     * tham số truyền vào không hợp lệ ở controller
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<List<FieldError>> ArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Exception error invalid input: ");
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        return ApiResponse.<List<FieldError>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessageConstant.ERROR_INPUT)
                .result(fieldErrorList)
                .build();
    }

    /**
     * vượt quá kích thước file
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse<String> maxFileExceeded(MaxUploadSizeExceededException ex) {
        log.error("Exception Max file: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessageConstant.ERROR_MAX_FILE)
                .result(ex.getMessage())
                .build();
    }

    /**
     * lỗi liên quan đến độ dài trong sql
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<String> SQLValidException(DataIntegrityViolationException ex) {
        log.error("Exception SQL length data: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.NOT_IMPLEMENTED.value())
                .message(ErrorMessageConstant.ERROR_INPUT_SQL)
                .result(ex.getMessage())
                .build();
    }

    /**
     * method not support
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<String> notSupportMethodException(HttpRequestMethodNotSupportedException ex) {
        log.error("Exception not support method: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.NOT_IMPLEMENTED.value())
                .message(ErrorMessageConstant.NOT_SUPPORT_METHOD)
                .result(ex.getMessage())
                .build();
    }

    /**
     * các Exception trong nghiệp vụ hệ thống
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ApiResponse<String> BadRequestException(ResponseStatusException ex) {
        log.error("Exception business: {}", ex.getReason());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getReason())
                .result(ex.getMessage())
                .build();
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<String> NoSuchException(NoSuchElementException ex) {
        log.error("Exception not found in database no such: {}",  ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.PRECONDITION_FAILED.value())
                .message(ErrorMessageConstant.NOT_FOUND_DATABASE_FIND_NO_SUCH)
                .result(ex.getMessage())
                .build();
    }

    /**
     * không tồn tại trong database khi thực hiện xóa
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ApiResponse<String> EmptyRequestException(EmptyResultDataAccessException ex) {
        log.error("Exception not found in database when delete: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.PRECONDITION_FAILED.value())
                .message(ErrorMessageConstant.NOT_FOUND_DATABASE_DELETE)
                .result(ex.getMessage())
                .build();
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm với các loại tìm kiếm cũ
     */
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ApiResponse<String> NotFoundException(ChangeSetPersister.NotFoundException ex) {
        log.error("Exception not found in database: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.PRECONDITION_FAILED.value())
                .message(ErrorMessageConstant.NOT_FOUND_DATABASE_FIND)
                .result(ex.getMessage())
                .build();
    }

    /**
     * tìm thấy nhiều hơn một row trong database
     */
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ApiResponse<String> nonUniqueResultException(IncorrectResultSizeDataAccessException ex) {
        log.error("Exception non unique result: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.PRECONDITION_FAILED.value())
                .message(ErrorMessageConstant.NON_UNIQUE_RESULT)
                .result(ex.getMessage())
                .build();
    }

    /**
     * lỗi null poiter
     */
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<String> NullPointerException(NullPointerException ex) {
        String logError = "Exception null pointer: " + ex.getMessage();
        log.error(logError);
        this.showAllLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.NOT_IMPLEMENTED.value())
                .message(ErrorMessageConstant.ERROR_COMMON_NULL_POINT)
                .result(logError)
                .build();
    }

    /**
     * lỗi cú pháp sql
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ApiResponse<String> SQLException(InvalidDataAccessResourceUsageException ex) {
        log.error("Exception SQL syntax: {}", ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.NOT_IMPLEMENTED.value())
                .message(ErrorMessageConstant.ERROR_COMMON_SQL)
                .result(ex.getMessage())
                .build();
    }

    /**
     * bắt tất cả các exception còn lại không kiểm soát được trong hệ thống
     */
    @ExceptionHandler(Exception.class)
    private ApiResponse<String> processIOException(Exception ex) {
        log.error("Exception error code: {}", ex.getMessage());
        this.showAllLogException(ex.getStackTrace());
        return ApiResponse.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ErrorMessageConstant.ERROR_SYSTEM)
                .result(ex.getMessage())
                .build();
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
