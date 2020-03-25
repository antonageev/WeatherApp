package com.antonageev.weatherapp;

import android.content.Intent;
import android.content.res.Configuration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    static final String PARCEL = "parcel";

    private TextView textContent;
    private TextView city;
    private TextView weatherDescription;
    private TextView temperature;
    private TextView wcf;
    private TextView wind;
    private TextView humidity;
    private Button buttonToday;
    private Button buttonTomorrow;
    private Button buttonAfterTomorrow;
    private Button aboutCity;
    private ImageButton citiesSelect;
    private ImageButton buttonSettings;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private String cityUrl;

    public static MainFragment create(Parcel parcel){
        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        mainFragment.setArguments(args);
        return mainFragment;
    }

    public Parcel getParcel() throws NullPointerException{
        return (Parcel) getArguments().getSerializable(PARCEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        Parcel parcel;
        try {
        parcel = getParcel();
        } catch (NullPointerException e){
            Log.w("MainFragment", "Перехват NullPointerException при запуске MainActivity из-за отсутствия parcel");
            parcel = (Parcel) getActivity().getIntent().getSerializableExtra(PARCEL);
        }

        initViews(layout);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            citiesSelect.setVisibility(View.INVISIBLE);
        }

        city.setText(parcel.getMapData().get(CITY));
        weatherDescription.setText(parcel.getMapData().get(WEATHER));
        temperature.setText(parcel.getMapData().get(TEMPERATURE));
        wcf.setText(parcel.getMapData().get(WIND_CHILL_FACTOR));
        humidity.setText(parcel.getMapData().get(HUMIDITY));
        wind.setText(parcel.getMapData().get(WIND));
        cityUrl = parcel.getMapData().get(CITY_URL);

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setButtonListeners();
    }

    private void initViews(View view){
        textContent = view.findViewById(R.id.textContent);
        buttonToday = view.findViewById(R.id.buttonToday);
        buttonTomorrow = view.findViewById(R.id.buttonTomorrow);
        buttonAfterTomorrow = view.findViewById(R.id.buttonAfterTomorrow);
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
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textContent.setText(R.string.stringContent);
            }
        });

        buttonTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getString(R.string.stringContentTom);
                textContent.setText(text);
            }
        });

        buttonAfterTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textContent.setText(R.string.stringContentAfterTom);
            }
        });
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
}
