package com.antonageev.weatherapp.model_current;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    @Expose
    private float temp;

    @SerializedName("feels_like")
    @Expose
    private float feelsLike;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("temp_min")
    @Expose
    private float tempMin;

    @SerializedName("temp_max")
    @Expose
    private float tempMax;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getTempMin() {
        return tempMin;
    }

    public  void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }
}
