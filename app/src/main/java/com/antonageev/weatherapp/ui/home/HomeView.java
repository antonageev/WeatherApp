package com.antonageev.weatherapp.ui.home;

import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.database.City;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;

public interface HomeView {
    void updateForecastList(WeatherForecast weatherForecast);
    void setTextViesFromParcel(Parcel parcel);
}
