package com.antonageev.weatherapp;

import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.google.gson.Gson;

import org.json.JSONObject;

public class WeatherParser {

    public static WeatherData renderWeatherData (JSONObject weatherData, String type){
        if (weatherData == null) return null;
        WeatherData resultWeatherData = null;
        Gson gson = new Gson();
        if (type == WeatherDataLoader.WEATHER_CURRENT_DATA){
            resultWeatherData = gson.fromJson(String.valueOf(weatherData), WeatherRequest.class);
        } else if (type == WeatherDataLoader.WEATHER_FORECAST_DATA){
            resultWeatherData = gson.fromJson(String.valueOf(weatherData), WeatherForecast.class);
        }
        return resultWeatherData;
    }
}
