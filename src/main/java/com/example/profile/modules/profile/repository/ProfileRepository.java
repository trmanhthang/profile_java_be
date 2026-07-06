package com.example.profile.modules.profile.repository;

import com.example.profile.modules.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
