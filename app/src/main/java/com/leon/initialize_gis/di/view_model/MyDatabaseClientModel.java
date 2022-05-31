package com.leon.initialize_gis.di.view_model;

import static com.leon.initialize_gis.helpers.Constants.DATABASE_NAME;

import android.content.Context;

import androidx.room.Room;

import com.leon.initialize_gis.utils.MyDatabase;

import javax.inject.Inject;

public class MyDatabaseClientModel {

    private static MyDatabaseClientModel instance;
    private final MyDatabase myDatabase;

    @Inject
    public MyDatabaseClientModel(Context context) {
        myDatabase = Room.databaseBuilder(context, MyDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();
    }

    public static synchronized MyDatabaseClientModel getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabaseClientModel(context);
        }
        return instance;
    }

    public static void migration(Context context) {
        Room.databaseBuilder(context, MyDatabase.class, DATABASE_NAME).
                fallbackToDestructiveMigration().
                addMigrations(MyDatabase.MIGRATION_1_2).
                allowMainThreadQueries().
                build();
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}