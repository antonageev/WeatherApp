package com.antonageev.weatherapp;

import com.antonageev.weatherapp.model_forecast.WeatherForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherForecast {
    @GET ("data/2.5/forecast")
    Call <WeatherForecast> loadWeather(@Query("q") String city, @Query("appid") String keyApi);
}
