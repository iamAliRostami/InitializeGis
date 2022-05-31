package com.leon.initialize_gis.di.module;


import com.leon.initialize_gis.di.view_model.ProgressModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ProgressModule {
    private final ProgressModel progressModel;

    public ProgressModule() {
        progressModel = ProgressModel.getInstance();
    }

    @Provides
    public ProgressModel providesCustomProgressModel() {
        return progressModel;
    }
}
