package com.example.profile.modules.user.response;

import com.example.profile.shared.enums.Roles;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AuthenticationResponse extends AccessTokenResponse {
    private String fullName;

    private String firstName;

    private String lastName;

    private Roles role;
}
