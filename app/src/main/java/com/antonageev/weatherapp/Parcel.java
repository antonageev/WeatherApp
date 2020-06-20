package com.antonageev.weatherapp;

import com.antonageev.weatherapp.database.City;

import java.io.Serializable;
import java.util.Map;

public class Parcel implements Serializable {
    private Map<String, String> mapData;
    private City cityData;

    public Parcel(Map<String, String> data){
        this.mapData = data;
    }

    public Parcel(City city){
        this.cityData = city;
    }

    public Map<String, String> getMapData() {
        return mapData;
    }

    public City getCityData() {
        return cityData;
    }
}
