package com.leon.initialize_gis.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.initialize_gis.tables.UsersPoints;

import java.util.List;

@Dao
public interface UsersPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsersPoint(UsersPoints userPoints);

    @Query("SELECT * FROM UsersPoints")
    List<UsersPoints> getUsersPoints();

    @Query("SELECT * FROM UsersPoints")
    List<UsersPoints> getUsersPointsByDate();

    @Query("select COUNT(*) From UsersPoints")
    int pointsCounter();

    @Query("select COUNT(*) From UsersPoints WHERE eshterak = :eshterak")
    int eshterakPointsCounter(String eshterak);

    @Query("Delete From UsersPoints WHERE eshterak = :eshterak")
    void deleteUserPoint(String eshterak);
}
