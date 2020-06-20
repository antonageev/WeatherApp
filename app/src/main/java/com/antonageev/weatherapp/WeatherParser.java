package com.antonageev.weatherapp;

import com.antonageev.weatherapp.database.City;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.google.gson.Gson;

import org.json.JSONObject;

public class WeatherParser {

    public static WeatherData renderWeatherData (JSONObject weatherData, String type){
        if (weatherData == null) return null;
        WeatherData resultWeatherData = null;
        Gson gson = new Gson();
        if (type.equals(WeatherDataLoader.WEATHER_CURRENT_DATA)){
            resultWeatherData = gson.fromJson(String.valueOf(weatherData), WeatherRequest.class);
        } else if (type.equals(WeatherDataLoader.WEATHER_FORECAST_DATA)){
            resultWeatherData = gson.fromJson(String.valueOf(weatherData), WeatherForecast.class);
        }
        return resultWeatherData;
    }

    public static City createCityFromWeatherRequest(WeatherRequest weatherRequest){
        City city = new City();
        city.setCityName(weatherRequest.getName());
        city.setDescription(weatherRequest.getWeather()[0].getDescription());
        city.setTempMax(weatherRequest.getMain().getTemp());
        city.setWcf(weatherRequest.getMain().getFeelsLike());
        city.setHumidity(weatherRequest.getMain().getHumidity());
        city.setDegrees(weatherRequest.getWind().getDeg());
        city.setWindSpeed(weatherRequest.getWind().getSpeed());
        city.setDateTime((long)weatherRequest.getDt() * 1000L);
        city.setIdResponse(weatherRequest.getWeather()[0].getId());

        return city;
    }
}
