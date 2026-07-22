package com.example.profile.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseDto {
    private Long id;

    private String publicId;

    private String createdId;

    private String createdBy;

    private LocalDateTime createdAt;

    private String modifiedId;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

    private boolean active;
}
