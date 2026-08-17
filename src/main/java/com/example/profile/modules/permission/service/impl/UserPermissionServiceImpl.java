package com.example.profile.modules.permission.service.impl;

import com.example.profile.cache.api.ApiCache;
import com.example.profile.cache.user.UserPermissionCache;
import com.example.profile.modules.api.dto.ApiCacheDto;
import com.example.profile.modules.permission.dto.PermissionCacheDto;
import com.example.profile.modules.permission.service.IUserPermissionService;
import com.example.profile.shared.enums.MethodApi;
import com.example.profile.shared.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements IUserPermissionService {
    private final UserPermissionCache userPermissionCache;

    private final ApiCache apiCache;

    private final AntPathMatcher pathMatcher;


    @Override
    public boolean hasPermission(Long userId, Roles role, MethodApi method, String path) {

        if (role.equals(Roles.ADMIN)) {
            return true;
        }

        List<PermissionCacheDto> permissions = this.userPermissionCache.findByUserId(userId);
        ApiCacheDto api = this.apiCache.findByUriAndMethod(
                path,
                method
        );

        boolean matchApiDefault = api.getUseDefault()
                                     .contains(role);

        if (matchApiDefault) {
            return true;
        }


        return permissions.stream()
                          .anyMatch(permission -> permission.getMethod()
                                                            .name()
                                                            .equalsIgnoreCase(method.name()) && pathMatcher.match(
                                  permission.getApi(),
                                  path
                          ));
    }
}
