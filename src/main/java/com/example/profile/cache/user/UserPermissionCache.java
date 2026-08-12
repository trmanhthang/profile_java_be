package com.example.profile.cache.user;

import com.example.profile.modules.permission.dto.PermissionCacheDto;
import com.example.profile.modules.permission.entity.UserPermission;
import com.example.profile.modules.permission.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserPermissionCache {
    private final UserPermissionRepository userPermissionRepository;

    @Cacheable(
            value = "permission",
            key = "#userId")
    public List<PermissionCacheDto> findByUserId(Long userId) {
        log.info(
                "cacheable permission with user id: {}",
                userId
        );

        List<UserPermission> permissions = this.userPermissionRepository.findAllByUserId(userId);

        return permissions.stream()
                          .map((PermissionCacheDto::build))
                          .toList();
    }
}
