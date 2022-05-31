package com.leon.initialize_gis.di.module;

import android.content.Context;


import com.leon.initialize_gis.di.view_model.SharedPreferenceModel;
import com.leon.initialize_gis.enums.SharedReferenceNames;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {
    private final SharedPreferenceModel sharedPreferenceModel;

    public SharedPreferenceModule(Context context, SharedReferenceNames sharedReferenceNames) {
        sharedPreferenceModel = SharedPreferenceModel.getInstance(context, sharedReferenceNames.getValue());
    }

    @Provides
    public SharedPreferenceModel providesSharedPreferenceModel() {
        return sharedPreferenceModel;
    }
}
