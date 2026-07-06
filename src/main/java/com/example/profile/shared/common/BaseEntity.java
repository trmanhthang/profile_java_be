package com.example.profile.shared.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", length = 36, nullable = false, unique = true)
    private final String publicId = UUID.randomUUID().toString();

    @Column(nullable = false, length = 36, name = "created_id")
    @CreatedBy
    private String createdId;

    /**
     * Tên người tạo tự thêm
     * */
    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "modified_id")
    private String modifiedId;

    /**
     * Tên người chỉnh sửa cuối tự thêm
     * */
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean active;
}
