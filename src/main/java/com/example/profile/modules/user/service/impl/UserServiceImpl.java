package com.example.profile.modules.user.service.impl;

import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.modules.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
}
