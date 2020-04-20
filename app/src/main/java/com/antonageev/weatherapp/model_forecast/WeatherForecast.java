package com.antonageev.weatherapp.model_forecast;

import android.os.Parcelable;

import com.antonageev.weatherapp.WeatherData;

import java.io.Serializable;

public class WeatherForecast implements WeatherData, Serializable {
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
