package com.example.profile.modules.api.repository;

import com.example.profile.modules.api.entity.Api;
import com.example.profile.shared.enums.MethodApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
    Optional<Api> findByUriAndMethod(String uri, MethodApi method);
}
