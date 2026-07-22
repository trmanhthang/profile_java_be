package com.example.profile.modules.user.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class AccessTokenResponse {
    private String publicId;

    private String accessToken;
}
