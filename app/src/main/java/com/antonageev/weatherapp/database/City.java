package com.antonageev.weatherapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (indices = {@Index(value = {"city_name"})})
public class City implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long idCity;

    @ColumnInfo(name = "city_name")
    private String cityName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "temp_max")
    private float tempMax;

    @ColumnInfo(name = "wcf")
    private float wcf;

    @ColumnInfo(name = "humidity")
    private int humidity;

    @ColumnInfo(name = "speed")
    private float windSpeed;

    @ColumnInfo(name = "deg")
    private int degrees;

    @ColumnInfo(name = "dateTime")
    private long dateTime;

    @ColumnInfo(name = "id_response")
    private int idResponse;

    public long getIdCity() {
        return idCity;
    }

    public void setIdCity(long idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getWcf() {
        return wcf;
    }

    public void setWcf(float wcf) {
        this.wcf = wcf;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getIdResponse() {
        return idResponse;
    }

    public void setIdResponse(int idResponse) {
        this.idResponse = idResponse;
    }
}
