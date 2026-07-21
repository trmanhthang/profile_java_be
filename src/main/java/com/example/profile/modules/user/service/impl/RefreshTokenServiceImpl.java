package com.example.profile.modules.user.service.impl;

import com.example.profile.config.redis.RedisService;
import com.example.profile.modules.user.service.IRefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    @Value("${app.jwtRefreshTokenExpirationInMs}")
    private long refreshTokenExpiration;

    private static final String PREFIX = "refresh_token:";

    private final RedisService redisService;

    @Override
    @Async
    public void save(String publicId, String refreshToken) {
        try {
            log.info("refreshTokenExpiration={}", refreshTokenExpiration);
            this.redisService.set(PREFIX + publicId, refreshToken, Duration.ofMillis(refreshTokenExpiration));
        } catch (RedisConnectionFailureException exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void addCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", token)
                .httpOnly(true)
                .secure(false )
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
