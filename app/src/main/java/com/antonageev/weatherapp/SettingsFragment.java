package com.antonageev.weatherapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Button backButton;
    private Switch switchDarkTheme;
    private RadioButton windMetersPerSecond;
    private RadioButton windKmPerHour;
    private RadioButton tempCels;
    private RadioButton tempFh;

    private SettingsHandler settingsHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsHandler = SettingsHandler.getInstance();
        initViews(view);
        setListeners();

        switchDarkTheme.setChecked(settingsHandler.isDarkTheme());
        setTextToSwitcher();
        windMetersPerSecond.setChecked(settingsHandler.isMetersPerSecondChecked());
        windKmPerHour.setChecked(settingsHandler.isKmPerHourChecked());
        tempCels.setChecked(settingsHandler.isCelsiusChecked());
        tempFh.setChecked(settingsHandler.isFhChecked());
    }

    private void initViews(View view){
        backButton = view.findViewById(R.id.backButton);
        switchDarkTheme = view.findViewById(R.id.switchDarkTheme);
        windMetersPerSecond = view.findViewById(R.id.radioButtonMS);
        windKmPerHour = view.findViewById(R.id.radioButtonKmHour);
        tempCels = view.findViewById(R.id.radioButtonCelsius);
        tempFh = view.findViewById(R.id.radioButtonFahrenheit);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getActivity().finish();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        switchDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextToSwitcher();
                settingsHandler.setDarkTheme(switchDarkTheme.isChecked());
            }
        });
        windMetersPerSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windMetersPerSecond.isChecked()) {
                    settingsHandler.setMetersPerSecondChecked(true);
                    settingsHandler.setKmPerHourChecked(false);
                }
            }
        });
        windKmPerHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windKmPerHour.isChecked()){
                    settingsHandler.setKmPerHourChecked(true);
                    settingsHandler.setMetersPerSecondChecked(false);
                }
            }
        });
        tempCels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempCels.isChecked()){
                    settingsHandler.setCelsiusChecked(true);
                    settingsHandler.setFhChecked(false);
                }
            }
        });
        tempFh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempFh.isChecked()){
                    settingsHandler.setFhChecked(true);
                    settingsHandler.setCelsiusChecked(false);
                }
            }
        });
    }

    private void setTextToSwitcher() {
        if (switchDarkTheme.isChecked()) {
            switchDarkTheme.setText(R.string.textSwitchDarkThemeOn);
        } else {
            switchDarkTheme.setText(R.string.textSwitchDarkThemeOff);
        }
    }
}
