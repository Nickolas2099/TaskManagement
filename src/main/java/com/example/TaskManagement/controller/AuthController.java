package com.example.TaskManagement.controller;

import com.example.TaskManagement.domain.api.auth.AuthenticationReq;
import com.example.TaskManagement.domain.api.auth.RegistrationReq;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Авторизация", description = "API для регистрации и авторизации пользователей")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегестрирован"),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Response> register(@Validated @RequestBody RegistrationReq request) {

        log.info("START endpoint register, request: {}", request);
        ResponseEntity<Response> response = authService.register(request);
        log.info("END endpoint register");
        return response;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Аутенификация пользователя", description = "Авторизаует пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь c указанной почтой не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationReq request) {

        log.info("START endpoint authenticate, request: {}", request);
        ResponseEntity<Response> response = authService.authenticate(request);
        log.info("END endpoint authenticate");
        return response;
    }

}
