package com.example.profile.modules.profile.entity;

import com.example.profile.shared.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_profile")
public class Profile extends BaseEntity {

    @Column(name = "full_name")
    private String fullName;

    private String email;

    private String phone;

    private String bio;

    private String address;

    private String avatar;

    private String title;

    private String description;
}
