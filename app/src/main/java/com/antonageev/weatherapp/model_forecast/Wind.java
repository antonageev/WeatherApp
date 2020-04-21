package com.antonageev.weatherapp.model_forecast;

import java.io.Serializable;

public class Wind implements Serializable {
    private float speed;
    private int deg;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}
