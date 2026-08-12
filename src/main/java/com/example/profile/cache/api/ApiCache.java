package com.example.profile.cache.api;

import com.example.profile.modules.api.dto.ApiCacheDto;
import com.example.profile.modules.api.entity.Api;
import com.example.profile.modules.api.repository.ApiRepository;
import com.example.profile.shared.enums.MethodApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApiCache {
    private final ApiRepository apiRepository;

    @Cacheable(
            value = "api",
            key = "#uri + ':' + #method",
            unless = "#result == null")
    public ApiCacheDto findByUriAndMethod(String uri, MethodApi method) {
        log.info(
                "cacheable api with uri: {} and method: {}",
                uri,
                method
        );

        Optional<Api> api = this.apiRepository.findByUriAndMethod(
                uri,
                method
        );

        return api.map(ApiCacheDto::build)
                  .orElse(null);
    }
}
