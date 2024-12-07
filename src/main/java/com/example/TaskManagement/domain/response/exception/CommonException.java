package com.example.TaskManagement.domain.response.exception;

import com.example.TaskManagement.domain.constant.Code;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommonException extends RuntimeException {

    Code code;
    String techMessage;
    String userMessage;
    HttpStatus httpStatus;
}
