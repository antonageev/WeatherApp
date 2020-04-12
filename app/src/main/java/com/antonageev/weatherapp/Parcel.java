package com.antonageev.weatherapp;

import java.io.Serializable;
import java.util.Map;

public class Parcel implements Serializable {
    private Map<String, String> mapData;

    public Parcel(Map<String, String> data){
        this.mapData = data;
    }

    public Map<String, String> getMapData() {
        return mapData;
    }
}
