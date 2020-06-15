package com.antonageev.weatherapp.model_current;

import com.antonageev.weatherapp.WeatherData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest implements WeatherData {

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("dt")
    @Expose
    private int dt;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }
}
