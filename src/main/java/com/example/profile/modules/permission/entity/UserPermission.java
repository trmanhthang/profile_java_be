package com.example.profile.modules.permission.entity;

import com.example.profile.modules.api.entity.Api;
import com.example.profile.shared.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_user_permission")
public class UserPermission extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "api_id", nullable = false)
    private Api api;
}
