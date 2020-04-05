package com.antonageev.weatherapp;

import java.io.Serializable;
import java.util.Map;

public class Parcel implements Serializable {
    private Map<String, String> mapData;

    Parcel(Map<String, String> data){
        this.mapData = data;
    }

    Map<String, String> getMapData() {
        return mapData;
    }
}
