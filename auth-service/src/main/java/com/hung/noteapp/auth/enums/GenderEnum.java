package com.hung.noteapp.auth.enums;

public enum GenderEnum {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    private final int value;

    GenderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GenderEnum fromValue(int value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.value == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value);
    }
}
