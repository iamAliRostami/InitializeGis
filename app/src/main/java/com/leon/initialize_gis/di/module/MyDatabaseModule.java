package com.leon.initialize_gis.di.module;

import android.content.Context;


import com.leon.initialize_gis.di.view_model.MyDatabaseClientModel;
import com.leon.initialize_gis.utils.MyDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class MyDatabaseModule {
    private final MyDatabase myDatabase;

    public MyDatabaseModule(Context context) {
        this.myDatabase = MyDatabaseClientModel.getInstance(context).getMyDatabase();
    }

    @Provides
    public MyDatabase providesMyDatabase() {
        return myDatabase;
    }
}
