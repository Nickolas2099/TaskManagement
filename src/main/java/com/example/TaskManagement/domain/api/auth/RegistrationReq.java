package com.example.TaskManagement.domain.api.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationReq {

    String email;

    String password;

    String firstname;

    String surname;

}
