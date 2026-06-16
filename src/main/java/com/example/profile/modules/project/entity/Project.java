package com.example.profile.modules.project.entity;

import com.example.profile.entity.BaseEntity;
import jakarta.persistence.Column;

public class Project extends BaseEntity {
    private String title;

    private String description;

    private String thumbnail;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "demo_url")
    private String demoUrl;
}
