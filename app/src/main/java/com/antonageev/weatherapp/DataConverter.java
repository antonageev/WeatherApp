package com.antonageev.weatherapp;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.antonageev.weatherapp.model_forecast.ListWeather;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

public class DataConverter {

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private static DataConverter instance;

    public static DataConverter getInstance() {
        if (instance == null) instance = new DataConverter();
        return instance;
    }

    private DataConverter() {
        MainActivity.getViewModelComponent().injectToDataConverter(this);
    }

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    AppCompatActivity activity;

    public List<Map<String, String>> convertForecastToListMap(WeatherForecast forecast) {
        String tempDegrees;
        if ((sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)).equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
            tempDegrees = "\u2109"; //F
        } else {
            tempDegrees = "\u2103"; //C
        }

        SimpleDateFormat formatDay = new SimpleDateFormat("EEEE,", Locale.getDefault());
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        List<Map<String, String>> resultList = new ArrayList<>();
        for (ListWeather listWeather : forecast.getListWeather()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(listWeather.getWeather()[0].getId()));
            map.put("day", formatDay.format(new Date((long) listWeather.getDt() * 1000L)));
            map.put("time", formatTime.format(new Date((long) listWeather.getDt() * 1000L)));
            map.put("weather", listWeather.getWeather()[0].getDescription());
            map.put("maxTemperature", String.format(Locale.getDefault(), "%.0f",
                    MeasurementsConverter.tempFromKelvinToSelectedMeasurement(listWeather.getMain().getTempMax(),
                            sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC) )) +" "+ tempDegrees);
            resultList.add(map);
        }
        return resultList;
    }

    public Map<String, String> convertParcelUsingPreferences(Parcel parcel) {

        Map<String, String> resultMap = new HashMap<>();

        if (parcel != null) {
            String tempDegrees, windUnits;
            if ((sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)).equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
                tempDegrees = "\u2109"; //F
                windUnits = activity.getResources().getString(R.string.windMilesPerHour);
            } else {
                tempDegrees = "\u2103"; //C
                windUnits = activity.getResources().getString(R.string.windMetersPerSecond);
            }

            float localTempMax = MeasurementsConverter.tempFromKelvinToSelectedMeasurement(parcel.getCityData().getTempMax(), sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));
            float localWcf = MeasurementsConverter.tempFromKelvinToSelectedMeasurement(parcel.getCityData().getWcf(), sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));
            float localWindSpeed = MeasurementsConverter.windFromMSToSelectedMeasurement(parcel.getCityData().getWindSpeed(), sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));

            resultMap.put(CITY , parcel.getCityData().getCityName());
            resultMap.put(WEATHER, parcel.getCityData().getDescription());
            resultMap.put(TEMPERATURE, String.format(Locale.getDefault(), "%.0f %s", localTempMax, tempDegrees));
            resultMap.put(WIND_CHILL_FACTOR, String.format(Locale.getDefault(),"%s %.0f %s", activity.getString(R.string.wcf_text), localWcf, tempDegrees));
            resultMap.put(HUMIDITY, String.format(Locale.getDefault(),"%s: %d %s", activity.getResources().getString(R.string.stringHumid),
                    parcel.getCityData().getHumidity(), "%"));
            resultMap.put(WIND, String.format(activity.getString(R.string.windDirection), assignWindDirection(parcel.getCityData().getDegrees()),
                    String.format(Locale.getDefault(),"%.1f",localWindSpeed), windUnits));


            //TODO воткнуть загрузку картинки (через RX?)
//        if (parcel.getCityData().getIdResponse() > 0){
//            Picasso.get()
//                    .load(MapWeatherLinks.getLinkFromMap(parcel.getCityData().getIdResponse() / 100))
//                    .resize(100, 100)
//                    .centerCrop()
//                    .transform(new CircleTransformation())
//                    .into(imageView);
//        }
        }

       return resultMap;

    }

    private String assignWindDirection(int degrees){
        if (degrees > 22 && degrees <= 67) return activity.getResources().getStringArray(R.array.windDirection)[0];
        if (degrees > 68 && degrees <= 112) return activity.getResources().getStringArray(R.array.windDirection)[1];
        if (degrees > 112 && degrees <= 157) return activity.getResources().getStringArray(R.array.windDirection)[2];
        if (degrees > 157 && degrees <= 202) return activity.getResources().getStringArray(R.array.windDirection)[3];
        if (degrees > 202 && degrees <= 247) return activity.getResources().getStringArray(R.array.windDirection)[4];
        if (degrees > 247 && degrees <= 292) return activity.getResources().getStringArray(R.array.windDirection)[5];
        if (degrees > 292 && degrees <= 337) return activity.getResources().getStringArray(R.array.windDirection)[6];
        if (degrees > 337 || degrees <= 22) return activity.getResources().getStringArray(R.array.windDirection)[7];
        return "unknown";
    }

}
