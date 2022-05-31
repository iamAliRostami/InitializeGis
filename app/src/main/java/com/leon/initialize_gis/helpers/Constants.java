package com.leon.initialize_gis.helpers;

import android.Manifest;

import com.leon.initialize_gis.di.component.ActivityComponent;
import com.leon.initialize_gis.di.component.ApplicationComponent;

import java.util.ArrayList;

public class Constants {
    public static final int TOAST_TEXT_SIZE = 20;

    public static final String FONT_NAME = "fonts/font_1.ttf";
    public static final String DATABASE_NAME = "MyDatabase_1";


    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BW_UPDATES = 10000;
    public static final long FASTEST_INTERVAL = 10000;

    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

}
