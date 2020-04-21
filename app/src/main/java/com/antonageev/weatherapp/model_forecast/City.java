package com.antonageev.weatherapp.model_forecast;

import java.io.Serializable;

public class City implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
