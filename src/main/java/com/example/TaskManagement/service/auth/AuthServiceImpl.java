package com.example.TaskManagement.service.auth;

import com.example.TaskManagement.domain.api.auth.AuthenticationReq;
import com.example.TaskManagement.domain.api.auth.RegistrationReq;
import com.example.TaskManagement.domain.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    @Override
    public ResponseEntity<Response> register(RegistrationReq request) {

        return null;
    }

    @Override
    public ResponseEntity<Response> authenticate(AuthenticationReq request) {

        return null;
    }
}
