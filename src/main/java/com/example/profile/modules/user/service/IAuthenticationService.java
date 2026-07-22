package com.example.profile.modules.user.service;

import com.example.profile.modules.user.request.LoginRequest;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.response.AccessTokenResponse;
import com.example.profile.modules.user.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthenticationService {
    void register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request, HttpServletResponse response);

    AccessTokenResponse refresh(String refreshToken);
}
