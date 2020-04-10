package com.antonageev.weatherapp.model_current;

public class WeatherRequest {
    private Main main;
    private Weather[] weather;
    private Wind wind;
    private String name;
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
