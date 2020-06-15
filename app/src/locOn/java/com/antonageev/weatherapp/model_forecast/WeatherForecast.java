package com.antonageev.weatherapp.model_forecast;

import com.antonageev.weatherapp.WeatherData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherForecast implements WeatherData, Serializable {

    @SerializedName("list")
    @Expose
    private ListWeather[] listWeather;

    @SerializedName("city")
    @Expose
    private City city;

    public ListWeather[] getListWeather() {
        return listWeather;
    }

    public void setListWeather(ListWeather[] listWeather) {
        this.listWeather = listWeather;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
