package com.example.TaskManagement.service.security.utility;

import com.example.TaskManagement.domain.entity.User;
import com.example.TaskManagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilitySecurityServiceImpl implements UtilitySecurityService {

    private final UserRepository userRepo;

    @Override
    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("администратор"));
    }

    @Override
    public User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User haven't found with email: " + email)
        );
    }
}
