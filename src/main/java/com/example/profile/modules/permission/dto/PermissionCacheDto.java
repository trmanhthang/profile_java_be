package com.example.profile.modules.permission.dto;

import com.example.profile.modules.permission.entity.UserPermission;
import com.example.profile.shared.enums.MethodApi;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermissionCacheDto {
    private Long id;

    private Long userId;

    private String api;

    private MethodApi method;

    public static PermissionCacheDto build(UserPermission userPermission) {
        return PermissionCacheDto.builder()
                                 .id(userPermission.getId())
                                 .userId(userPermission.getUserId())
                                 .api(userPermission.getApi()
                                                    .getUri())
                                 .method(userPermission.getApi()
                                                       .getMethod())
                                 .build();
    }
}
