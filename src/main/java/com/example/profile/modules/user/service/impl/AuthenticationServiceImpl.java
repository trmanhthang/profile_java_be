package com.example.profile.modules.user.service.impl;

import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;

    @Override
    public void register(RegisterRequest request) {

    }
}
