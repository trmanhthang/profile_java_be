package com.example.profile.modules.permission.service;

import com.example.profile.shared.enums.MethodApi;
import com.example.profile.shared.enums.Roles;

public interface IUserPermissionService {
    boolean hasPermission(Long userId, Roles role, MethodApi method, String path);
}
