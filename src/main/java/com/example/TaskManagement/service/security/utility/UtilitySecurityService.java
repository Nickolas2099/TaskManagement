package com.example.TaskManagement.service.security.utility;

import com.example.TaskManagement.domain.entity.User;

public interface UtilitySecurityService {

    boolean isAdmin();

    User getCurrentUser();

}
