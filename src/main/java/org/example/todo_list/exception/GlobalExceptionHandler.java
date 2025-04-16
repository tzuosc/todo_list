package org.example.todo_list.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理数据层异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataException(DataIntegrityViolationException ex) {
        log.error("数据库异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, "数据冲突，请检查任务名称唯一性"));
    }

    // 自定义的异常处理

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(BaseException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage()));
    }

    // 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("参数错误")
                ));
        return ApiResponse.error(400, "参数校验失败", errors);
    }

    // 兜底异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error("系统异常: ", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "系统繁忙，请稍后重试"));
    }
}
