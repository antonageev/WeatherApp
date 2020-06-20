package com.antonageev.weatherapp;

public class MeasurementsConverter {

    public static float tempFromKelvinToSelectedMeasurement(float kelvin, String SELECTED_MEASUREMENT) {
        if (SELECTED_MEASUREMENT.equals(WeatherDataLoader.MEASURE_METRIC)) {
            return kelvin - 273.15f; // return Celsius
        } else if (SELECTED_MEASUREMENT.equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
            return kelvin * 1.8f - 459f; //return Fahrenheit
        }
        return 0f;
    }

    public static float windFromMSToSelectedMeasurement(float defaultMS, String SELECTED_MEASUREMENT) {
        if (SELECTED_MEASUREMENT.equals(WeatherDataLoader.MEASURE_METRIC)) {
            return defaultMS; // return meters per second
        } else if (SELECTED_MEASUREMENT.equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
            return defaultMS*0.44f; //return miles per hour
        }
        return 0f;
    }

}
