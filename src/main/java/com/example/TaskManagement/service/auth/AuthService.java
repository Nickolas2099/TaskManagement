package com.example.TaskManagement.service.auth;

import com.example.TaskManagement.domain.api.auth.AuthenticationReq;
import com.example.TaskManagement.domain.api.auth.RegistrationReq;
import com.example.TaskManagement.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface AuthService {


    ResponseEntity<Response> register(RegistrationReq request);

    ResponseEntity<Response> authenticate(AuthenticationReq request);
}
