package com.antonageev.weatherapp.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherData;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherListAdapter;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.WeatherUpdateService;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

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
    RecyclerView recyclerView;
    WeatherListAdapter weatherListAdapter;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private BroadcastReceiver broadcastReceiver;

    private String cityUrl;

    private Parcel localParcel = null;
    private int index = 0;

    private List<Map<String, String>> forecast = new ArrayList<>();

    public HomeFragment(Parcel parcel){
        this.localParcel = parcel;
    }

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void getOrInitParcel(Bundle savedInstanceState) {

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Log.wtf(TAG, "SharedViewModel: " + model.getSavedParcel().getValue());
        localParcel = model.getSavedParcel().getValue();

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
                localParcel = new Parcel(createInitialMapData());
                index = Integer.parseInt(localParcel.getMapData().get("index"));
                Log.w(TAG , "parcel created: " + localParcel);
            } catch (NullPointerException e){
                Log.w(TAG, " Перехват NullPointerException при запуске MainActivity из-за проблем создания parcel");
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getOrInitParcel(savedInstanceState);
        Log.wtf(TAG, "parcel object: " + localParcel);
        Log.wtf(TAG , "parcel state CITY: " + localParcel.getMapData().get(CITY));
        Log.wtf(TAG, "index state: " + index);

        setTextViesFromParcel(localParcel);

        citiesSelect.setVisibility(View.INVISIBLE);
        buttonSettings.setVisibility(View.INVISIBLE);
        aboutCity.setVisibility(View.INVISIBLE);

        initRecyclerView(new ArrayList<Map<String, String>>());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WeatherForecast weatherForecast = (WeatherForecast) intent.getSerializableExtra("cityForecast");
                if (weatherForecast != null) {
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, HH:mm", Locale.getDefault());
                    List<Map<String, String>> resultList = new ArrayList<>();
                    for (com.antonageev.weatherapp.model_forecast.List list : weatherForecast.getList()) {
                        Map<String, String> map = new HashMap<>();
                        map.put("day", format.format(new Date((long) list.getDt() * 1000L)));
                        map.put("weather", list.getWeather()[0].getDescription());
                        map.put("maxTemperature", String.format(Locale.getDefault(), "%.0f", list.getMain().getTemp_max()) + " \u2103");
                        resultList.add(map);
                    }
                    weatherListAdapter.weatherListDataChange(resultList);
                }
            }
        };

        updateForecast();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.WEATHER_FORECAST_INTENT_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
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

    private void initRecyclerView(List<Map<String, String>> forecast) {
        LinearLayoutManager ltManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(ltManager);
        weatherListAdapter = new WeatherListAdapter(forecast);
        recyclerView.setAdapter(weatherListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PARCEL, localParcel);
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
        recyclerView = view.findViewById(R.id.recyclerView);
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

    private void updateForecast() {

        String localCity;
        if (localParcel != null) {
            localCity = localParcel.getMapData().get("city");
        } else {
            localCity = city.getText().toString();
        }

        Intent intent = new Intent(getActivity(), WeatherUpdateService.class);
        intent.putExtra("city", localCity);
        getActivity().startService(intent);

    }

}