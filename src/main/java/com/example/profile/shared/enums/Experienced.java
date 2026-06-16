package com.example.profile.shared.enums;

import lombok.Getter;

@Getter
public enum Experienced {
    FRESHER("fresher"),
    JUNIOR("junior");

    private final String value;

    Experienced(String value) {
        this.value = value;
    }
}
