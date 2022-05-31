package com.leon.initialize_gis.di.component;


import com.leon.initialize_gis.di.module.CustomDialogModule;
import com.leon.initialize_gis.di.module.LocationTrackingModule;
import com.leon.initialize_gis.di.view_model.LocationTrackingGoogle;
import com.leon.initialize_gis.di.view_model.LocationTrackingGps;
import com.leon.initialize_gis.utils.custom_dialog.LovelyStandardDialog;

import dagger.Component;

@Component(modules = {CustomDialogModule.class, LocationTrackingModule.class})
public interface ActivityComponent {

    LovelyStandardDialog LovelyStandardDialog();

    LocationTrackingGps LocationTrackingGps();

    LocationTrackingGoogle LocationTrackingGoogle();

}
