package com.leon.initialize_gis.tables;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "UsersPoints", indices = @Index(value = {"id"}, unique = true))
public class UsersPoints {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String eshterak;
    public double x;//Longitude
    public double y;//Latitude
    public String date;
    public String time;
    public String phoneDateTime;
    public String locationDateTime;
}
