package com.example.profile.modules.user.service.impl;

import com.example.profile.cache.user.UserCache;
import com.example.profile.config.jwt.JwtService;
import com.example.profile.config.security.CustomUserDetailsService;
import com.example.profile.config.security.UserPrincipal;
import com.example.profile.modules.user.dto.UserCacheDto;
import com.example.profile.modules.user.entity.User;
import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.modules.user.request.LoginRequest;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.response.AccessTokenResponse;
import com.example.profile.modules.user.response.AuthenticationResponse;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.modules.user.service.IRefreshTokenService;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import com.example.profile.shared.enums.Roles;
import com.example.profile.shared.exception.custom.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserCache userCache;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final IRefreshTokenService refreshTokenService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void register(RegisterRequest request) {
        UserCacheDto userExists = this.userCache.findByUsername(request.getUsername());

        if (userExists != null) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User();
        String fullName = request.getFirstName() + " " + request.getLastName();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(fullName);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Roles.USER);

        this.userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request, HttpServletResponse response) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserCacheDto user = this.userCache.findByUsername(request.getUsername());

        UserPrincipal userPrincipal = new UserPrincipal(user);

        String accessToken = this.jwtService.generateAccessToken(userPrincipal);

        String refreshToken = this.jwtService.generateRefreshToken(userPrincipal);

        this.refreshTokenService.save(
                userPrincipal.getPublicId(),
                refreshToken
        );

        this.refreshTokenService.addCookie(
                response,
                refreshToken
        );

        return AuthenticationResponse.builder()
                                     .publicId(user.getPublicId())
                                     .accessToken(accessToken)
                                     .role(user.getRole())
                                     .fullName(user.getFullName())
                                     .firstName(user.getFirstName())
                                     .lastName(user.getLastName())
                                     .build();
    }

    @Override
    public AccessTokenResponse refresh(String refreshToken) {
        this.jwtService.validateToken(refreshToken);

        String uid = this.jwtService.extractClaim(
                refreshToken,
                claims -> claims.get(
                        "uid",
                        String.class
                )
        );

        String redisToken = this.refreshTokenService.get(uid);

        if (redisToken == null || redisToken.isBlank()) {
            throw new InsufficientAuthenticationException(AuthenticationMessageConstant.TOKEN_MISSING);
        }

        if (!redisToken.equals(refreshToken)) {
            throw new BadCredentialsException(AuthenticationMessageConstant.TOKEN_INVALID);
        }

        UserPrincipal user = this.customUserDetailsService.loadUserByPublicId(uid);

        if (!this.jwtService.isTokenValid(
                refreshToken,
                user
        )) {
            throw new BadCredentialsException(AuthenticationMessageConstant.TOKEN_INVALID);
        }

        String accessToken = this.jwtService.generateAccessToken(user);

        return AccessTokenResponse.builder()
                                  .publicId(user.getPublicId())
                                  .accessToken(accessToken)
                                  .build();
    }
}
