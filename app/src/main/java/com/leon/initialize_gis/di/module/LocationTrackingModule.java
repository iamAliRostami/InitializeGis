package com.leon.initialize_gis.di.module;

import static com.leon.initialize_gis.utils.CheckSensor.checkSensor;

import android.app.Activity;

import com.leon.initialize_gis.di.view_model.LocationTrackingGoogle;
import com.leon.initialize_gis.di.view_model.LocationTrackingGps;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationTrackingModule {
    private LocationTrackingGoogle locationTrackingGoogle;
    private LocationTrackingGps locationTrackingGps;

    public LocationTrackingModule(Activity activity) {
        if (checkSensor(activity))
            locationTrackingGoogle = LocationTrackingGoogle.getInstance(activity);
        else
            locationTrackingGps = LocationTrackingGps.getInstance();
    }

    @Provides
    public LocationTrackingGps providesLocationTrackingGps() {
        return locationTrackingGps;
    }


    @Provides
    public LocationTrackingGoogle providesLocationTrackingGoogle() {
        return locationTrackingGoogle;
    }
}
