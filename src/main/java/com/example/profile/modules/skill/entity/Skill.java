package com.example.profile.modules.skill.entity;

import com.example.profile.common.entity.BaseEntity;
import com.example.profile.shared.enums.Experienced;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ma_skill")
public class Skill extends BaseEntity {

    private String name;

    private Experienced level;
}
