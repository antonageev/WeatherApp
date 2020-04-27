package com.antonageev.weatherapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index(value = {"city_name"})})
public class City {

    @PrimaryKey(autoGenerate = true)
    public long idCity;

    @ColumnInfo(name = "city_name")
    public String cityName;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "temp_max")
    public float tempMax;

    @ColumnInfo(name = "wcf")
    public float wcf;

    @ColumnInfo(name = "humidity")
    public int humidity;

    @ColumnInfo(name = "speed")
    public float windSpeed;

    @ColumnInfo(name = "deg")
    public int degrees;

    public long dateTime;

    @ColumnInfo(name = "id_response")
    public int idResponse;
}
