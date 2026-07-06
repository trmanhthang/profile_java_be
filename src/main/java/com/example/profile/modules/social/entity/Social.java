package com.example.profile.modules.social.entity;

import com.example.profile.shared.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_social")
public class Social extends BaseEntity {
    private String name;

    private String url;

    private String icon;
}
