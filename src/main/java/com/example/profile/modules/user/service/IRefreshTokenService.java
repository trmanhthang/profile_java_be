package com.example.profile.modules.user.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IRefreshTokenService {
    void save(String publicId, String refreshToken);

    String get(String publicId);

    void addCookie(HttpServletResponse response, String token);
}
