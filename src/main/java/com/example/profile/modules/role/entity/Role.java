package com.example.profile.modules.role.entity;

import com.example.profile.entity.BaseEntity;
import com.example.profile.shared.enums.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_role")
public class Role extends BaseEntity {

    @Column(nullable = false, name = "role", unique = true)
    private Roles role;

    private String name;
}
