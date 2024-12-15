package com.example.TaskManagement.domain.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationReq {

    @Email(message = "invalid email format")
    @Size(max = 50, message = "email out of symbols border. max is 50")
    @NotBlank(message = "email is required")
    String email;

    @Size(max = 30, message = "password out of symbols border. max is 30")
    @NotBlank(message = "password is required")
    String password;

    @Size(max = 30, message = "firstname out of symbols border. max is 30")
    @NotBlank(message = "firstname is required")
    String firstname;

    @Size(max = 30, message = "surname out of symbols border. max is 30")
    @NotBlank(message = "surname is required")
    String surname;

}
