package com.antonageev.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCityActivity extends AppCompatActivity {

    private Button backButton;
    private ListView listView;
    private List<Map<String, String>> citiesWeatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        citiesWeatherList = initHardCodedCitiesList();
        initViews();
        setListeners();

        SimpleAdapter adapter = new SimpleAdapter(this, citiesWeatherList,
                R.layout.list_item_cities,
                new String[]{"city", "weather", "temperature"},
                new int[]{R.id.text_view_city, R.id.text_view_weather, R.id.text_view_temperature});
        listView.setAdapter(adapter);

    }

    private List<Map<String, String>> initHardCodedCitiesList() {
        List<Map<String, String>> citiesList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("city", "NewYork");
        map.put("weather", "Cloudy");
        map.put("temperature", "13 C");
        map.put("wcf", "5 C");
        map.put("humidity", "Humidity: 65%");
        map.put("wind", "Wind: SW, 3 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlNewYork));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("city", "Tokyo");
        map.put("weather", "Shiny");
        map.put("temperature", "8 C");
        map.put("wcf", "0 C");
        map.put("humidity", "Humidity: 50%");
        map.put("wind", "Wind: E, 1 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlTokyo));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("city", "Moscow");
        map.put("weather", "Rainy");
        map.put("temperature", "6 C");
        map.put("wcf", "-2 C");
        map.put("humidity", "Humidity: 90%");
        map.put("wind", "Wind: N, 8 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlMoscow));
        citiesList.add(map);

        return citiesList;
    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listCities);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> rowData = (Map<String, String>) parent.getItemAtPosition(position);
                Intent intentResult = new Intent();
                intentResult.putExtra("city", rowData.get("city"));
                intentResult.putExtra("weather", rowData.get("weather"));
                intentResult.putExtra("temperature", rowData.get("temperature"));
                intentResult.putExtra("wcf", rowData.get("wcf"));
                intentResult.putExtra("humidity", rowData.get("humidity"));
                intentResult.putExtra("wind", rowData.get("wind"));
                intentResult.putExtra("cityUrl", rowData.get("cityUrl"));
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });
    }
}
