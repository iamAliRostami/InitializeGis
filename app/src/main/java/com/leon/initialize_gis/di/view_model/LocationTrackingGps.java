package com.leon.initialize_gis.di.view_model;


import static com.leon.initialize_gis.helpers.Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.leon.initialize_gis.helpers.Constants.MIN_TIME_BW_UPDATES;
import static com.leon.initialize_gis.helpers.MyApplication.getContext;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import com.leon.initialize_gis.helpers.MyApplication;
import com.leon.initialize_gis.interfaces.ILocationTracking;


public class LocationTrackingGps extends Service implements LocationListener, ILocationTracking {
    private static LocationTrackingGps instance = null;
    private volatile static Location location;
    protected LocationManager locationManager;
    private double latitude;
    private double longitude;

    public LocationTrackingGps() {
        getLocation();
    }

    public static synchronized LocationTrackingGps getInstance() {
        if (instance == null) {
            instance = new LocationTrackingGps();
            instance.addLocation(location);
        }
        return instance;
    }

    public static void setInstance(LocationTrackingGps instance) {
        LocationTrackingGps.instance = instance;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Location getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            boolean checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (checkGPS || checkNetwork) {
                if (checkGPS) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (checkNetwork) {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    @Override
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Override
    public double getAccuracy() {
        return location.getAccuracy();
    }

    @Override
    public void addLocation(Location location) {
        if (location != null && (location.getLatitude() != 0 || location.getLongitude() != 0)) {
            LocationTrackingGps.location = location;
        }
    }

    @Override
    public Location getCurrentLocation() {
        return getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        instance.addLocation(location);
    }

    public void stopListener() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTrackingGps.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
