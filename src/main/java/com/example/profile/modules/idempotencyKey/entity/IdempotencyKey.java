package com.example.profile.modules.idempotencyKey.entity;

import com.example.profile.shared.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "idempotency_key",
        indexes = {@Index(
                name = "idx_expires_at",
                columnList = "expires_at")},
        uniqueConstraints = {@UniqueConstraint(
                name = "uk_idempotency_key",
                columnNames = "idempotency_key")})
public class IdempotencyKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "idempotency_key",
            nullable = false,
            length = 64)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyStatus status;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(
            name = "request_hash",
            nullable = false,
            length = 64)
    private String requestHash;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String response;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
