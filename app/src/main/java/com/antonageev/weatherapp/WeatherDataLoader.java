package com.antonageev.weatherapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDataLoader {

    public static final String API_KEY = "639a7024d266b4113f5eb00f0a3fe1f0";

    public static final String KEY_MEASUREMENT = "measurement";
    public static final String MEASURE_METRIC = "metric";
    public static final String MEASURE_IMPERIAL = "imperial";

    public static final String KEY_LANGUAGE = "lang";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_RU = "ru";

    public static final String WEATHER_CURRENT_DATA = "weather";
    public static final String WEATHER_FORECAST_DATA = "forecast";


    public static JSONObject getJSONdata(String city, final String REQUEST_WEATHER_TYPE) {
        String requestedUrl = String.format("https://api.openweathermap.org/data/2.5/%s?q=%s&units=%s&appid=%s", REQUEST_WEATHER_TYPE,
                city, MEASURE_METRIC, API_KEY);
        JSONObject jsonObject;

        try {
            final URL uri = new URL(requestedUrl);
            HttpsURLConnection urlConnection;
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String tempString;
            StringBuilder rawData = new StringBuilder();

            while ((tempString = in.readLine()) != null) {
                rawData.append(tempString).append("\n");
            }

            in.close();
            Log.wtf("Static getJSONdata: ", rawData.toString());

            jsonObject = new JSONObject(rawData.toString());
            return jsonObject;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
