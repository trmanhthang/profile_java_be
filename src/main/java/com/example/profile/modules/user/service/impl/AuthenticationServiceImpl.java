package com.example.profile.modules.user.service.impl;

import com.example.profile.config.jwt.JwtService;
import com.example.profile.config.security.UserPrincipal;
import com.example.profile.modules.user.entity.User;
import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.modules.user.request.LoginRequest;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.response.AuthenticationResponse;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.modules.user.service.IRefreshTokenService;
import com.example.profile.shared.enums.Roles;
import com.example.profile.shared.exception.custom.UserNotFoundException;
import com.example.profile.shared.exception.custom.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final IRefreshTokenService refreshTokenService;

    @Override
    public void register(RegisterRequest request) {
        Optional<User> userExists = this.userRepository.findByUsername(request.getUsername());

        if (userExists.isPresent()) {
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(UserNotFoundException::new);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        String accessToken = this.jwtService.generateAccessToken(userPrincipal);

        String refreshToken = this.jwtService.generateRefreshToken(userPrincipal);

        this.refreshTokenService.save(userPrincipal.getPublicId(), refreshToken);

        this.refreshTokenService.addCookie(response, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .role(user.getRole())
                .fullName(user.getFullName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
