package com.example.profile.modules.user.entity;

import com.example.profile.shared.common.BaseEntity;
import com.example.profile.shared.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_user")
public class User extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false)
    @Size(min = 8)
    @NotBlank
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false )
    private Roles role;

    @Size(min = 10, max = 11)
    private String phone;

    private LocalDateTime dob;
}
