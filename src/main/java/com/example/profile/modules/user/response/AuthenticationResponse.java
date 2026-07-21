package com.example.profile.modules.user.response;

import com.example.profile.shared.enums.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticationResponse {
    private String fullName;

    private String firstName;

    private String lastName;

    private String accessToken;

    private Roles role;
}
