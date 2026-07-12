package com.example.profile.modules.user.service.impl;

import com.example.profile.modules.user.entity.User;
import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.shared.enums.Roles;
import com.example.profile.shared.exception.custom.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

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
}
