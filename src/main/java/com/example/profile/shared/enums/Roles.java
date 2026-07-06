package com.example.profile.shared.enums;

import lombok.Getter;

@Getter
public enum Roles {
    SUPPER_ADMIN("supper_admin"),
    ADMIN("admin"),
    MANAGER("manager"),
    USER("user");

    private final String value;

    Roles(String value) {
        this.value = value;
    }
}
