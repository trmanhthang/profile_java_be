package com.example.profile.modules.project.entity;

import com.example.profile.shared.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_project")
public class Project extends BaseEntity {
    private String title;

    private String description;

    private String thumbnail;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "demo_url")
    private String demoUrl;
}
