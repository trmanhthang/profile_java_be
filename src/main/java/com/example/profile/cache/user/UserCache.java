package com.example.profile.cache.user;

import com.example.profile.modules.user.dto.UserCacheDto;
import com.example.profile.modules.user.entity.User;
import com.example.profile.modules.user.repository.UserRepository;
import com.example.profile.shared.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCache {
    private final UserRepository userRepository;

    @Cacheable(value = "user", key = "#username")
    public UserCacheDto findByUsername(String username) {
        log.info(
                "cacheable user with username: {}",
                username
        );
        User user = this.userRepository.findByUsername(username)
                                       .orElseThrow(UserNotFoundException::new);
        return UserCacheDto.build(user);
    }
}
