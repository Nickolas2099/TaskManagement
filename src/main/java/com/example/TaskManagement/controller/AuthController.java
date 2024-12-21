package com.example.TaskManagement.controller;

import com.example.TaskManagement.domain.api.auth.AuthenticationReq;
import com.example.TaskManagement.domain.api.auth.RegistrationReq;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.service.auth.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Validated @RequestBody RegistrationReq request) {

        log.info("START endpoint register, request: {}", request);
        ResponseEntity<Response> response = authService.register(request);
        log.info("END endpoint register");
        return response;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationReq request) {

        log.info("START endpoint authenticate, request: {}", request);
        ResponseEntity<Response> response = authService.authenticate(request);
        log.info("END endpoint authenticate");
        return response;
    }

}
