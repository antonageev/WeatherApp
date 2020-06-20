package com.antonageev.weatherapp.ui.home;

import com.antonageev.weatherapp.Parcel;

import java.util.List;
import java.util.Map;

public interface HomeView {
//    void updateForecastList(List<Map<String, String>> weatherForecastMap);
    void setTextViesFromMap(Map<String, String> weatherMap);
}
