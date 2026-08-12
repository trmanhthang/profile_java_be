package com.example.profile.modules.api.dto;

import com.example.profile.modules.api.entity.Api;
import com.example.profile.shared.enums.MethodApi;
import com.example.profile.shared.enums.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ApiCacheDto {
    private Long id;

    private String uri;

    private MethodApi method;

    private List<Roles> useDefault;

    public static ApiCacheDto build(Api api) {
        return ApiCacheDto.builder()
                          .id(api.getId())
                          .uri(api.getUri())
                          .method(api.getMethod())
                          .useDefault(api.getUseDefault())
                          .build();
    }
}
