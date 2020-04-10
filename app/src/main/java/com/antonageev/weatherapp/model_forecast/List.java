package com.antonageev.weatherapp.model_forecast;


public class List {
    private Main main;
    private Weather[] weather;
    private Wind wind;
    private int dt;
    private String dt_txt;

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

    public  String getDt_txt() {
        return dt_txt;
    }

    public  void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
