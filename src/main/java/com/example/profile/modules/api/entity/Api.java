package com.example.profile.modules.api.entity;

import com.example.profile.shared.common.BaseEntity;
import com.example.profile.shared.enums.MethodApi;
import com.example.profile.shared.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_api", indexes = {
        @Index(name = "idx_api", columnList = "uri, method")
})
public class Api extends BaseEntity {
    @Column(nullable = false, unique = true)
    @NotBlank
    private String uri;

    private String name;

    private String description;

    private String version;

    @Enumerated(EnumType.STRING)
    private MethodApi method;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ma_api_default_role", joinColumns = @JoinColumn(name = "api_id"))
    @Column(name = "use_default")
    @Enumerated(EnumType.STRING)
    private List<Roles> useDefault;
}
