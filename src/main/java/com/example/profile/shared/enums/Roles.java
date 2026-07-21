package com.example.profile.shared.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("admin"),
    MANAGER("manager"),
    MEMBERSHIP("membership"),
    USER("user");

    private final String value;

    Roles(String value) {
        this.value = value;
    }
}
