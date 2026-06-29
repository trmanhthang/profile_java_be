package com.example.profile.modules.user.service;

import com.example.profile.modules.user.request.RegisterRequest;

public interface IAuthenticationService {
    void register(RegisterRequest request);
}
