package com.example.profile.modules.role.repository;

import com.example.profile.modules.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
