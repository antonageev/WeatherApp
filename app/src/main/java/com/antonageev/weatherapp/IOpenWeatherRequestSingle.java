package com.antonageev.weatherapp;

import com.antonageev.weatherapp.model_current.WeatherRequest;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherRequestSingle {
    @GET ("data/2.5/weather")
    Single<WeatherRequest> loadWeather(@Query("q") String city, @Query("lang") String lang, @Query("appid") String keyApi);
}
