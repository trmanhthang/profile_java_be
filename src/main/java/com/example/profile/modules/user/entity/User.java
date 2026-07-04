package com.example.profile.modules.user.entity;

import com.example.profile.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_user")
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    @Size(min = 8)
    private String username;

    @Column(name = "password")
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

    @Size(min = 10, max = 11)
    private String phone;
}
