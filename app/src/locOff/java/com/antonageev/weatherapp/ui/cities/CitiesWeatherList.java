package com.antonageev.weatherapp.ui.cities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CitiesWeatherList implements Serializable {
    private List<Map<String, String>> list;

    public CitiesWeatherList(List<Map<String, String>> mapData) {
        this.list = mapData;
    }

    public CitiesWeatherList() {
        this.list = new LinkedList<>();
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    public void addItem(Map<String, String> mapData){
        list.add(mapData);
    }

    public boolean isCityInList(String city){
        for (Map<String, String> map: list) {
            if (map.containsValue(city)) return true;
        }
        return false;
    }

}
