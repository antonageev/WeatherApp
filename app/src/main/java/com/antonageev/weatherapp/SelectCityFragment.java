package com.antonageev.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.observer.Publisher;
import com.antonageev.weatherapp.observer.PublisherGetter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.antonageev.weatherapp.MainFragment.PARCEL;
/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private final Handler handler = new Handler();

    Parcel currentParcel;
    private MaterialButton backButton;
    private MaterialButton findButton;
    private TextInputEditText editTextCity;
    private List<Map<String, String>> citiesWeatherList;
    private RecyclerView recyclerView;

    private Publisher publisher;

    private JSONObject weatherJSONdata;

    private boolean mDualPane;

    public static SelectCityFragment create(int index){
        SelectCityFragment selectCityFragment = new SelectCityFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        selectCityFragment.setArguments(args);
        return selectCityFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            publisher = ((PublisherGetter) context).getPublisher();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        citiesWeatherList = initCitiesList();
        initViews(view);
        setListeners();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        initRecyclerView();

        if (mDualPane){
            backButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initRecyclerView() {
        CityListAdapter cityListAdapter = new CityListAdapter(citiesWeatherList);
        LinearLayoutManager lt = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(lt);
        cityListAdapter.setOnItemClickListener(new CityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Snackbar.make(view, getResources().getString(R.string.snackBarSure), BaseTransientBottomBar.LENGTH_SHORT).
                        setAction(getResources().getString(R.string.confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentParcel = new Parcel(citiesWeatherList.get(position));
                                showMainFragment(currentParcel);
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(cityListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(getActivity().getDrawable(R.drawable.separator)));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void showMainFragment(Parcel parcel) {
        Log.wtf(TAG, "showMainFragment - mDualPane: " + mDualPane);
        if (mDualPane) {
            Log.wtf(TAG , "mainFragment: " + getFragmentManager().findFragmentById(R.id.mainFragment));
            publisher.notify(parcel);

        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MainActivity.class);
            intent.putExtra(PARCEL, parcel);
            startActivity(intent);
        }
    }

    private void initViews(View view){
        backButton = view.findViewById(R.id.backButton);
        findButton = view.findViewById(R.id.findButton);
        editTextCity = view.findViewById(R.id.editTextCity);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeatherData(editTextCity.getText().toString());
            }
        });
    }

    private void updateWeatherData(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                weatherJSONdata = WeatherDataLoader.getJSONdata(city);
                if (weatherJSONdata != null){
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(String.valueOf(weatherJSONdata), WeatherRequest.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showMainFragment(new Parcel(renderWeather(weatherRequest)));
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(getView(),"City "+ city + " not found", BaseTransientBottomBar.LENGTH_SHORT).show();
                            editTextCity.requestFocus();
                        }
                    });
                    Log.e(TAG, "Connection failed");
                }
            }
        }).start();
    }

    private Map<String, String> renderWeather(WeatherRequest weatherRequest){
        Map<String, String> localMap = new HashMap<>();
        localMap.put("index", "0");
        localMap.put("city", weatherRequest.getName());
        localMap.put("weather", weatherRequest.getWeather()[0].getMain());
        localMap.put("temperature", String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp()) + " \u2103");
        localMap.put("wcf",  String.format(Locale.getDefault(),"%.0f",weatherRequest.getMain().getFeels_like()) + " \u2103");
        localMap.put("humidity", "Humidity: " + weatherRequest.getMain().getHumidity()+"%");
        localMap.put("wind", String.format("Wind: %s, %s m/s", assignWindDirection(weatherRequest.getWind().getDeg()),
                String.format(Locale.getDefault(),"%.1f",weatherRequest.getWind().getSpeed())));
        localMap.put("cityUrl", getResources().getString(R.string.urlMoscow));

        return localMap;
    }

    private String assignWindDirection(int degrees){
        if (degrees > 22 && degrees <= 67) return getResources().getStringArray(R.array.windDirection)[0];
        if (degrees > 68 && degrees <= 112) return getResources().getStringArray(R.array.windDirection)[1];
        if (degrees > 112 && degrees <= 157) return getResources().getStringArray(R.array.windDirection)[2];
        if (degrees > 157 && degrees <= 202) return getResources().getStringArray(R.array.windDirection)[3];
        if (degrees > 202 && degrees <= 247) return getResources().getStringArray(R.array.windDirection)[4];
        if (degrees > 247 && degrees <= 292) return getResources().getStringArray(R.array.windDirection)[5];
        if (degrees > 292 && degrees <= 337) return getResources().getStringArray(R.array.windDirection)[6];
        if (degrees > 337 || degrees <= 22) return getResources().getStringArray(R.array.windDirection)[7];
        return "unknown";
    }


    private List<Map<String, String>> initCitiesList() {
        List<Map<String, String>> citiesList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("index", "0");
        map.put("city", "Moscow");
        map.put("weather", "Rainy");
        map.put("temperature", "6 C");
        map.put("wcf", "-2 C");
        map.put("humidity", "Humidity: 90%");
        map.put("wind", "Wind: N, 8 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlMoscow));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("index", "1");
        map.put("city", "NewYork");
        map.put("weather", "Cloudy");
        map.put("temperature", "13 C");
        map.put("wcf", "5 C");
        map.put("humidity", "Humidity: 65%");
        map.put("wind", "Wind: SW, 3 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlNewYork));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("index", "2");
        map.put("city", "Tokyo");
        map.put("weather", "Shiny");
        map.put("temperature", "8 C");
        map.put("wcf", "0 C");
        map.put("humidity", "Humidity: 50%");
        map.put("wind", "Wind: E, 1 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlTokyo));
        citiesList.add(map);

        return citiesList;
    }
}
