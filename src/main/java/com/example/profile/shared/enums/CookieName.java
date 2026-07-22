package com.example.profile.shared.enums;

import lombok.Getter;

@Getter
public enum CookieName {
    REFRESH_TOKEN("refresh_token");

    private final String value;

    CookieName(String value) {
        this.value = value;
    }
}
