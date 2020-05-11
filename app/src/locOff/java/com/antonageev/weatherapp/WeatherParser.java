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
//Добавить проверку на возврат нуля в случае не определенного города (тест на ЖК ГН)
        City city = new City();
        city.cityName = weatherRequest.getName();
        city.description = weatherRequest.getWeather()[0].getDescription();
        city.tempMax = weatherRequest.getMain().getTemp();
        city.wcf = weatherRequest.getMain().getFeelsLike();
        city.humidity = weatherRequest.getMain().getHumidity();
        city.degrees = weatherRequest.getWind().getDeg();
        city.windSpeed = weatherRequest.getWind().getSpeed();
        city.dateTime = (long)weatherRequest.getDt() * 1000L;
        city.idResponse = weatherRequest.getWeather()[0].getId();

        return city;
    }
}
