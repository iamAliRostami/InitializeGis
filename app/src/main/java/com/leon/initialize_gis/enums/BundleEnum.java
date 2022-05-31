package com.leon.initialize_gis.enums;

public enum BundleEnum {
    BILL_ID("bill_Id"),
    TRACK_NUMBER("trackNumber"),
    EXAMINER_DUTY("examinerDuty"),
    NEW_ENSHEAB("new_ensheab"),
    DATA("data"),
    READ_STATUS("readStatus"),
    THEME("theme"),
    ACCOUNT("ACCOUNT"),
    TYPE("type"),
    TITLE("title"),
    OTHER_TITLE("other_title"),
    LICENCE_TITLE("licence_title"),
    ON_OFFLOAD("ON_OFFLOAD"),
    POSITION("position"),
    SPINNER_POSITION("spinner_position"),
    COUNTER_STATE_POSITION("counterStatePosition"),
    COUNTER_STATE_CODE("counterStatePosition"),
    NUMBER("counterStateCode"),
    CURRENT_PAGE("number"),
    IMAGE_BITMAP("image_bitmap"),
    REQUEST("request"),
    ZONE_ID("zone_id"),
    IS_NEIGHBOUR("is_neighbour"),
    SERVICES("services");

    private final String value;

    BundleEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
