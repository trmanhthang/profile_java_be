package com.example.profile.modules.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotNull
    @NotBlank(message = "username không được để trống")
    private String username;

    @NotNull
    @NotBlank(message = "password không được để trống")
    private String password;
}
