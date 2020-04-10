package com.antonageev.weatherapp.model_forecast;

public class WeatherForecast {
    private List[] list;
    private City city;

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
