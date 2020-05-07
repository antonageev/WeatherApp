package com.antonageev.weatherapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class WeatherForecastLoader {
    private static final String API_KEY = "639a7024d266b4113f5eb00f0a3fe1f0";
    private static final String UNITS_METRIC = "metric";

    static JSONObject getJSONdata(String city) {
        String requestedUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&units=%s&appid=%s",
                city, UNITS_METRIC, API_KEY);
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
            Log.wtf("Static getJSONForecastData: ", rawData.toString());

            jsonObject = new JSONObject(rawData.toString());
            return jsonObject;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
