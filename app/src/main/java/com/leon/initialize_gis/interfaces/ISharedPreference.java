package com.leon.initialize_gis.interfaces;

/**
 * Created by Leon on 1/10/2018.
 */

public interface ISharedPreference {
    void putData(String key, String value);

    void putData(String key, int value);

    void putData(String key, boolean value);

    String getStringData(String key);

    int getIntData(String key);

    boolean getBoolData(String key);

    boolean checkIsNotEmpty(String key);
}
