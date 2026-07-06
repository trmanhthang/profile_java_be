package com.example.profile.modules.api.entity;

import com.example.profile.shared.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_api")
public class Api extends BaseEntity {
    @Column(nullable = false, unique = true)
    @NotBlank
    private String api;

    private String name;

    private String description;

    private String version;
}
