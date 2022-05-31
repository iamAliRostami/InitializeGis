package com.leon.initialize_gis.interfaces;

import android.location.Location;

public interface ILocationTracking {

    Location getLocation();

    double getLatitude();

    double getLongitude();

    double getAccuracy();

    void addLocation(Location location);

    Location getCurrentLocation(/*Context context*/);
}
