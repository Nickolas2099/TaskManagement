package com.example.TaskManagement.service.auth;

import com.example.TaskManagement.domain.api.auth.AuthResp;
import com.example.TaskManagement.domain.api.auth.AuthenticationReq;
import com.example.TaskManagement.domain.api.auth.RegistrationReq;
import com.example.TaskManagement.domain.constant.Code;
import com.example.TaskManagement.domain.entity.Role;
import com.example.TaskManagement.domain.entity.User;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.domain.response.SuccessResponse;
import com.example.TaskManagement.domain.response.error.Error;
import com.example.TaskManagement.domain.response.error.ErrorResponse;
import com.example.TaskManagement.repository.RoleRepository;
import com.example.TaskManagement.repository.UserRepository;
import com.example.TaskManagement.service.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService{

    RoleRepository roleRepository;
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authManager;

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public ResponseEntity<Response> register(RegistrationReq request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error(Error.builder()
                            .code(Code.BAD_REQUEST)
                            .techMessage("User with that email already exists")
                            .build())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.getRoleByTitle("пользователь").orElseThrow(
                () -> new EntityNotFoundException("role with title `пользователь` haven't found")
        );
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .roles(roles)
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new ResponseEntity<>(SuccessResponse.builder().data(AuthResp
                .builder()
                .token(jwt)
                .build()
        ).build(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> authenticate(AuthenticationReq request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("User haven't found with email: " + request.getEmail())
        );
        String jwt = jwtService.generateToken(user);
        return new ResponseEntity<>(SuccessResponse.builder().data(AuthResp
                .builder()
                .token(jwt)
                .build()
                ).build(), HttpStatus.OK);
    }
}
