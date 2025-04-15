package com.sber_ii_lab.enums;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter

public enum NewsType {
    LOCAL("Местные"),
    GLOBAL("Мировые");

    private final String displayName;

    NewsType(String displayName) {
        this.displayName = displayName;
    }

}
