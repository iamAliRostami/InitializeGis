package com.leon.initialize_gis.helpers;

import android.Manifest;

public class Constants {
    public static boolean exit = false;
    public static int POSITION = 0;
    public static int TRIAL_NUMBER = 100;
    public static String  USERNAME = "user1";
    public static String PASSWORD = "123456";


    public static final int HOME_FRAGMENT = 0;
    public static final int POINT_FRAGMENT = 1;
    public static final int REPORT_FRAGMENT = 2;
    public static final int EXPORT_FRAGMENT = 3;

    public static final int EXIT_POSITION = 4;
    public static final int TOAST_TEXT_SIZE = 20;

    public static final String FONT_NAME = "fonts/font_1.ttf";
    public static final String DATABASE_NAME = "MyDatabase_1";


    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BW_UPDATES = 10000;
    public static final long FASTEST_INTERVAL = 10000;

    public static final int GPS_CODE = 1231;

    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

}
