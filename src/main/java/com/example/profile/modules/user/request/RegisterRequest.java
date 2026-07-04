package com.example.profile.modules.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "username không được để trống!")
    private String username;

    @NotBlank(message = "password không được để trống!")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống!")
    private String firstName;

    @NotBlank(message = "")
    private String lastName;

    private String email;

    private String avatar;

    private String phone;
}
