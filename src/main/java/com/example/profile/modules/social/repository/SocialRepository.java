package com.example.profile.modules.social.repository;

import com.example.profile.modules.social.entity.Social;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Long> {
}
