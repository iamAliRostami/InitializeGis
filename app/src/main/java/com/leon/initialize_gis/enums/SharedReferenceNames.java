package com.leon.initialize_gis.enums;

public enum SharedReferenceNames {
    ACCOUNT();

    private final String value;

    SharedReferenceNames() {
        value = "com.app.leon.estimate_new.account_info";
    }

    public String getValue() {
        return value;
    }
}
