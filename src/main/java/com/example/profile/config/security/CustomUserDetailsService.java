package com.example.profile.config.security;
import com.example.profile.cache.user.UserCache;
import com.example.profile.modules.user.dto.UserCacheDto;
import com.example.profile.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCache userCache;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCacheDto user = this.userCache.findByUsername(username);

        return new UserPrincipal(user);
    }
}
