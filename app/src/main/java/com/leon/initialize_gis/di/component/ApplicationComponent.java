package com.leon.initialize_gis.di.component;


import com.leon.initialize_gis.di.module.MyDatabaseModule;
import com.leon.initialize_gis.di.module.ProgressModule;
import com.leon.initialize_gis.di.module.SharedPreferenceModule;
import com.leon.initialize_gis.di.view_model.ProgressModel;
import com.leon.initialize_gis.di.view_model.SharedPreferenceModel;
import com.leon.initialize_gis.helpers.MyApplication;
import com.leon.initialize_gis.utils.MyDatabase;

import dagger.Component;

@Component(modules = {MyDatabaseModule.class, SharedPreferenceModule.class, ProgressModule.class})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);


    MyDatabase MyDatabase();

    SharedPreferenceModel SharedPreferenceModel();

    ProgressModel ProgressModel();
}
