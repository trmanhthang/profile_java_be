package com.example.profile.modules.user.dto;

import com.example.profile.modules.user.entity.User;
import com.example.profile.shared.dto.BaseDto;
import com.example.profile.shared.enums.Roles;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserCacheDto extends BaseDto {
    private String username;

    private String password;

    private String email;

    private String fullName;

    private String firstName;

    private String lastName;

    private String avatar;

    private Roles role;

    private String phone;

    private LocalDateTime dob;

    private int version;

    private LocalDateTime passwordChangeAt;

    public static UserCacheDto build(User user) {
        return UserCacheDto.builder()
                           .id(user.getId())
                           .publicId(user.getPublicId())
                           .createdId(user.getCreatedId())
                           .createdBy(user.getCreatedBy())
                           .createdAt(user.getCreatedAt())
                           .modifiedId(user.getModifiedId())
                           .lastModifiedBy(user.getLastModifiedBy())
                           .lastModifiedAt(user.getLastModifiedAt())
                           .active(user.isActive())
                           .username(user.getUsername())
                           .password(user.getPassword())
                           .email(user.getEmail())
                           .fullName(user.getFullName())
                           .firstName(user.getFirstName())
                           .lastName(user.getLastName())
                           .avatar(user.getAvatar())
                           .role(user.getRole())
                           .phone(user.getPhone())
                           .dob(user.getDob())
                           .version(user.getVersion())
                           .passwordChangeAt(user.getPasswordChangeAt())
                           .build();
    }
}
