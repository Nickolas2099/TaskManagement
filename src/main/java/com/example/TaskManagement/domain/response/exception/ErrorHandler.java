package com.example.TaskManagement.domain.response.exception;

import com.example.TaskManagement.domain.constant.Code;
import com.example.TaskManagement.domain.response.error.Error;
import com.example.TaskManagement.domain.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        log.error("common error: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(Error.builder().code(ex.getCode()).techMessage(ex.getUserMessage())
                        .build()).build(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedErrorException(Exception ex) {
        ex.printStackTrace();
        log.error("internal server error: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.INTERNAL_SERVER_ERROR)
                .techMessage("Внутренняя ошибка сервиса").build()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error("Validation error! Field: {}, error message: {}", fieldName, errorMessage);
        });

        ErrorResponse response = ErrorResponse.builder()
                .error(Error.builder()
                        .code(Code.BAD_REQUEST)
                        .techMessage("Validation error")
                        .details(errors)
                        .build())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
