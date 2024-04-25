package com.example.ondemand.EnumClass;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnitOfMeasure {
    KM("KM"),
    MILE("MILE");

    private final String value;

    UnitOfMeasure(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UnitOfMeasure fromString(String value) {
        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid UnitOfMeasure value: " + value);
    }
}

