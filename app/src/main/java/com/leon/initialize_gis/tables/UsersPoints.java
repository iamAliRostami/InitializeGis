package com.leon.initialize_gis.tables;


import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.leon.initialize_gis.interfaces.ILocationTracking;
import com.leon.initialize_gis.utils.CalendarTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @SuppressLint("SimpleDateFormat")
    public void getDateInformation(ILocationTracking location) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss:SSS");
        final CalendarTool calendarTool = new CalendarTool();
        date = calendarTool.getIranianDate();
        phoneDateTime = dateFormatter.format(new Date(Calendar.getInstance().getTimeInMillis()));
        if (location != null)
            locationDateTime = dateFormatter.format(new Date(location.getCurrentLocation().getTime()));
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss:SSS");
        time = timeFormatter.format(new Date(Calendar.getInstance().getTimeInMillis()));
    }
}
