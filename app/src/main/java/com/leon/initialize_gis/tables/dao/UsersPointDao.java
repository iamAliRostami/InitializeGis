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
    Long insertUsersPoint(UsersPoints userPoints);

    @Query("SELECT * FROM UsersPoints")
    List<UsersPoints> getUsersPoints();

    @Query("SELECT * FROM UsersPoints")
    List<UsersPoints> getUsersPointsByDate();
}
