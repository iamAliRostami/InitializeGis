package com.leon.initialize_gis.helpers;

import android.Manifest;

import java.util.ArrayList;

public class Constants {
    public static final int TOAST_TEXT_SIZE = 20;

    public static final String FONT_NAME = "fonts/font_1.ttf";
    public static final String DATABASE_NAME = "MyDatabase_1";


    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BW_UPDATES = 10000;
    public static final long FASTEST_INTERVAL = 10000;

    public static final ArrayList<Integer> IS_MANE = new ArrayList<>();
    public static final String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[] PHOTO_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final String[] RECORD_AUDIO_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
//    public static ApplicationComponent applicationComponent;
//    public static ActivityComponent activityComponent;
}
