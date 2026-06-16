package com.example.profile.shared.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("admin"),
    USER("user");

    private final String value;

    Roles(String value) {
        this.value = value;
    }
}
