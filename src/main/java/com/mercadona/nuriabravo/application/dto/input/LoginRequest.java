package com.mercadona.nuriabravo.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "login.username.required")
    private String username;

    @NotBlank(message = "login.password.required")
    private String password;
}