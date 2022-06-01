package com.leon.initialize_gis.helpers;

import static com.leon.initialize_gis.enums.SharedReferenceNames.ACCOUNT;
import static com.leon.initialize_gis.helpers.Constants.FONT_NAME;
import static com.leon.initialize_gis.helpers.Constants.TOAST_TEXT_SIZE;
import static com.leon.initialize_gis.utils.CheckSensor.checkSensor;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.leon.initialize_gis.di.component.ActivityComponent;
import com.leon.initialize_gis.di.component.ApplicationComponent;
import com.leon.initialize_gis.di.component.DaggerActivityComponent;
import com.leon.initialize_gis.di.component.DaggerApplicationComponent;
import com.leon.initialize_gis.di.module.DialogModule;
import com.leon.initialize_gis.di.module.LocationTrackingModule;
import com.leon.initialize_gis.di.module.MyDatabaseModule;
import com.leon.initialize_gis.di.module.ProgressModule;
import com.leon.initialize_gis.di.module.SharedPreferenceModule;
import com.leon.initialize_gis.interfaces.ILocationTracking;

import es.dmoral.toasty.Toasty;

public class MyApplication extends Application {
    private static Context appContext;
    private static ApplicationComponent applicationComponent;
    private static ActivityComponent activityComponent;


    @Override
    public void onCreate() {
        appContext = getApplicationContext();
        Toasty.Config.getInstance().tintIcon(true)
                .setToastTypeface(Typeface.createFromAsset(appContext.getAssets(), FONT_NAME))
                .setTextSize(TOAST_TEXT_SIZE).allowQueue(true).apply();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .progressModule(new ProgressModule())
                .myDatabaseModule(new MyDatabaseModule(appContext))
                .sharedPreferenceModule(new SharedPreferenceModule(appContext, ACCOUNT)).build();
        applicationComponent.inject(this);
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public static void setActivityComponent(Activity activity) {
        activityComponent = DaggerActivityComponent.builder()
                .dialogModule(new DialogModule(activity))
                .locationTrackingModule(new LocationTrackingModule(activity)).build();
    }

    public static ILocationTracking getLocationTracker(Activity activity) {
        return checkSensor(activity) ? activityComponent.LocationTrackingGoogle() :
                activityComponent.LocationTrackingGps();
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static Context getContext() {
        return appContext;
    }

    public static String getAndroidVersion() {
        final String release = Build.VERSION.RELEASE;
        final int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }
}

