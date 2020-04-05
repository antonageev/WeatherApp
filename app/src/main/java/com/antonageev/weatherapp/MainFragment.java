package com.antonageev.weatherapp;

import android.content.Intent;
import android.content.res.Configuration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.antonageev.weatherapp.observer.Observer;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Observer {

    private final String TAG = this.getClass().getSimpleName();

    static final String PARCEL = "parcel";

    private TextView city;
    private TextView weatherDescription;
    private TextView temperature;
    private TextView wcf;
    private TextView wind;
    private TextView humidity;
    private Button aboutCity;
    private MaterialButton citiesSelect;
    private MaterialButton buttonSettings;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private String cityUrl;

    private Parcel localParcel = null;
    private int index = 0;

    private List<Map<String, String>> forecast = new ArrayList<>();

    int getLocalIndex(){
        return index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void getOrInitParcel(Bundle savedInstanceState) {
        Log.wtf(TAG, "getOrInit: Parcel: " + localParcel);
        Log.wtf(TAG, "getOrInit: savedInstState: " + savedInstanceState);
        if (localParcel == null && savedInstanceState != null){
            try {
                localParcel = (Parcel) savedInstanceState.getSerializable(PARCEL);
                index = Integer.parseInt(localParcel.getMapData().get("index"));
                Log.w(TAG, "parcel savedInstance: " + localParcel);
                Log.w(TAG, "savedInstance: " + savedInstanceState);
            } catch (NullPointerException e){
                Log.w(TAG , " Перехват NullPointerException при запуске MainActivity из-за отсутствия savedInstanceState");
            }
        }

        if (localParcel == null){
            try {
                localParcel = (Parcel) getActivity().getIntent().getSerializableExtra(PARCEL);
                index = Integer.parseInt(localParcel.getMapData().get("index"));
                Log.w(TAG , "parcel getIntent: " + localParcel);
                Log.w(TAG , "parcel from getIntent, getCity: " + localParcel.getMapData().get(CITY));
            } catch (NullPointerException e){
                Log.w(TAG, " Перехват NullPointerException при запуске MainActivity из-за отсутствия Intent");
            }
        }

        if (localParcel == null){
            try {
                localParcel = new Parcel(createInitialMapData());
                index = Integer.parseInt(localParcel.getMapData().get("index"));;
                Log.w(TAG , "parcel created: " + localParcel);
            } catch (NullPointerException e){
                Log.w(TAG, " Перехват NullPointerException при запуске MainActivity из-за проблем создания parcel");
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.wtf(TAG , "Main: mainFragment" + getFragmentManager().findFragmentById(R.id.mainFragment));
        initViews(view);
        setButtonListeners();
        getOrInitParcel(savedInstanceState);
        Log.wtf(TAG, "parcel object: " + localParcel);
        Log.wtf(TAG , "parcel state CITY: " + localParcel.getMapData().get(CITY));
        Log.wtf(TAG, "index state: " + index);

        setTextViesFromParcel(localParcel);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            citiesSelect.setVisibility(View.INVISIBLE);
        }

        forecast = initForecast();
        initRecyclerView(view);
    }

    private void setTextViesFromParcel(Parcel parcel) {
        city.setText(parcel.getMapData().get(CITY));
        weatherDescription.setText(parcel.getMapData().get(WEATHER));
        temperature.setText(parcel.getMapData().get(TEMPERATURE));
        wcf.setText(parcel.getMapData().get(WIND_CHILL_FACTOR));
        humidity.setText(parcel.getMapData().get(HUMIDITY));
        wind.setText(parcel.getMapData().get(WIND));
        cityUrl = parcel.getMapData().get(CITY_URL);
    }

    private void initRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager ltManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(ltManager);
        WeatherListAdapter weatherListAdapter = new WeatherListAdapter(forecast);
        recyclerView.setAdapter(weatherListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(PARCEL, localParcel);
        Log.d(TAG, "onSaveInstanceState -outState: " + outState);
        Log.d(TAG, "onSaveInstanceState -outState, parcel.getCity(): " + localParcel.getMapData().get(CITY));
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view){
        citiesSelect = view.findViewById(R.id.citiesSelect);
        buttonSettings = view.findViewById(R.id.buttonSettings);
        city = view.findViewById(R.id.city);
        weatherDescription = view.findViewById(R.id.weatherDescription);
        temperature = view.findViewById(R.id.temperature);
        wcf = view.findViewById(R.id.wcf);
        wind = view.findViewById(R.id.wind);
        humidity = view.findViewById(R.id.humidity);
        aboutCity = view.findViewById(R.id.aboutCity);
    }

    private void setButtonListeners() {
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSettings = new Intent(getContext(), SettingsActivity.class);
                startActivity(intentToSettings);
            }
        });
        citiesSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToCitiesSelect = new Intent(getContext(), SelectCityActivity.class);
                startActivity(intentToCitiesSelect);
            }
        });
        aboutCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(cityUrl);
                Intent intentViewCity = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentViewCity);
            }
        });
    }

    private Map<String, String> createInitialMapData(){
        Map<String, String> initMap = new HashMap<>();
        initMap.put("index", "0"); // "0" for "Moscow" as default
        initMap.put(CITY, city.getText().toString());
        initMap.put(WEATHER, weatherDescription.getText().toString());
        initMap.put(TEMPERATURE, temperature.getText().toString());
        initMap.put(WIND_CHILL_FACTOR, wcf.getText().toString());
        initMap.put(HUMIDITY, humidity.getText().toString());
        initMap.put(WIND, wind.getText().toString());
        initMap.put(CITY_URL, getResources().getString(R.string.urlMoscow));
        return initMap;
    }

    private List<Map<String, String>> initForecast(){

        String[] days = getResources().getStringArray(R.array.days);
        String[] weathers = getResources().getStringArray(R.array.weathers);
        String[] maxTemperatures = getResources().getStringArray(R.array.maxTemperatures);

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("day", days[0]);
        map.put("weather", weathers[0]);
        map.put("maxTemperature", maxTemperatures[0]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[1]);
        map.put("weather", weathers[1]);
        map.put("maxTemperature", maxTemperatures[1]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[2]);
        map.put("weather", weathers[2]);
        map.put("maxTemperature", maxTemperatures[2]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[3]);
        map.put("weather", weathers[3]);
        map.put("maxTemperature", maxTemperatures[3]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[4]);
        map.put("weather", weathers[4]);
        map.put("maxTemperature", maxTemperatures[4]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[5]);
        map.put("weather", weathers[5]);
        map.put("maxTemperature", maxTemperatures[5]);
        list.add(map);

        map = new HashMap<>();
        map.put("day", days[6]);
        map.put("weather", weathers[6]);
        map.put("maxTemperature", maxTemperatures[6]);
        list.add(map);

        return list;
    }

    @Override
    public void updateFields(Parcel parcel) {
        localParcel = parcel;
        setTextViesFromParcel(parcel);
    }
}
