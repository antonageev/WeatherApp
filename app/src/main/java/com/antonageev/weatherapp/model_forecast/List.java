package com.antonageev.weatherapp.model_forecast;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class List implements Serializable {
    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("dt")
    @Expose
    private int dt;

    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public  Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public  Wind getWind() {
        return wind;
    }

    public  void setWind(Wind wind) {
        this.wind = wind;
    }

    public  int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public  String getDtTxt() {
        return dtTxt;
    }

    public  void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }
}
