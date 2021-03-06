package com.leon.initialize_gis.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.leon.initialize_gis.tables.UsersPoints;
import com.leon.initialize_gis.tables.dao.UsersPointDao;

import org.jetbrains.annotations.NotNull;

@Database(entities = {UsersPoints.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NotNull SupportSQLiteDatabase database) {
        }
    };

    public abstract UsersPointDao usersPointDao();

}
