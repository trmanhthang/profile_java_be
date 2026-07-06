package com.example.profile.modules.project.repository;

import com.example.profile.modules.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
