package com.leon.initialize_gis.enums;

public enum BundleEnum {
    LATITUDE("latitude"),
    LONGITUDE("longitude");

    private final String value;

    BundleEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
