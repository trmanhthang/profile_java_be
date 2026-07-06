package com.example.profile.modules.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotNull
    @NotBlank(message = "username không được để trống!")
    private String username;

    @NotNull
    @NotBlank(message = "password không được để trống!")
    private String password;

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    private String email;

    private String avatar;

    private String phone;
}
