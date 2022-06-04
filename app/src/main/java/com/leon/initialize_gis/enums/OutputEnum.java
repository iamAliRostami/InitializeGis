package com.leon.initialize_gis.enums;

public enum OutputEnum {
    CSV("CSV"),
    XLS("XLS"),
    XLSX("XLSX");

    private final String value;

    OutputEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
