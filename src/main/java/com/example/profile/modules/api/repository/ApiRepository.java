package com.example.profile.modules.api.repository;

import com.example.profile.modules.api.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {

}
