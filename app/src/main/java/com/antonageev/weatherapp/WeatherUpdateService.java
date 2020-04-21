package com.antonageev.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.antonageev.weatherapp.model_forecast.WeatherForecast;

import org.json.JSONObject;

public class WeatherUpdateService extends IntentService {

    private final String TAG = this.getClass().getSimpleName();

    public WeatherUpdateService(){
        super("WeatherUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String city = intent.getStringExtra("city");

            JSONObject forecastJSONData = WeatherDataLoader.getJSONdata(city, WeatherDataLoader.WEATHER_FORECAST_DATA);
            WeatherForecast weatherForecast = (WeatherForecast) WeatherParser.renderWeatherData(forecastJSONData, WeatherDataLoader.WEATHER_FORECAST_DATA);
            if (weatherForecast != null){
                Intent intentOutput = new Intent(MainActivity.WEATHER_FORECAST_INTENT_FILTER);
                intentOutput.putExtra("cityForecast", weatherForecast);
                sendBroadcast(intentOutput);
            }
        } catch (NullPointerException e){
            Log.d(TAG, "onHandleIntent: Null Pointer (no city) or IOException (no data loaded)");
            e.printStackTrace();
        }
    }
}
