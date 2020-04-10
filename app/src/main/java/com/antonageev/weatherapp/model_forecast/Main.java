package com.antonageev.weatherapp.model_forecast;

public class Main {
    private float temp;
    private float feels_like;
    private int humidity;
    private float temp_min;
    private float temp_max;

    public float getTemp() {
        return temp;
    }

    public  void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public  void setFeels_like(float feels_like) {
        this.feels_like = feels_like;
    }

    public  int getHumidity() {
        return humidity;
    }

    public  void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public   float getTemp_min() {
        return temp_min;
    }

    public  void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public  float getTemp_max() {
        return temp_max;
    }

    public  void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }
}
