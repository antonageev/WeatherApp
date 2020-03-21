package com.antonageev.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Button backButton;
    private Switch switchDarkTheme;
    private RadioButton windMetersPerSecond;
    private RadioButton windKmPerHour;
    private RadioButton tempCels;
    private RadioButton tempFh;

    private SettingsHandler settingsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsHandler = SettingsHandler.getInstance();
        initViews();
        setListeners();
        String savedInstance;
        if (savedInstanceState == null){
            savedInstance = "First launch";
        } else {
            savedInstance = "Repeated launch";
        }
        Toast.makeText(this, TAG + " " + savedInstance +" - onCreate", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate()");


        switchDarkTheme.setChecked(settingsHandler.isDarkTheme());
        setTextToSwitcher();
        windMetersPerSecond.setChecked(settingsHandler.isMetersPerSecondChecked());
        windKmPerHour.setChecked(settingsHandler.isKmPerHourChecked());
        tempCels.setChecked(settingsHandler.isCelsiusChecked());
        tempFh.setChecked(settingsHandler.isFhChecked());

    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        switchDarkTheme = findViewById(R.id.switchDarkTheme);
        windMetersPerSecond = findViewById(R.id.radioButtonMS);
        windKmPerHour = findViewById(R.id.radioButtonKmHour);
        tempCels = findViewById(R.id.radioButtonCelsius);
        tempFh = findViewById(R.id.radioButtonFahrenheit);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, TAG + " - onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, TAG + " - onSaveInstanceState", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this, TAG + " - onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestoreInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, TAG + " - onRestart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, TAG + " - onResume", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, TAG + " - onPause", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, TAG + " - onStop", Toast.LENGTH_SHORT).show();
        Log.wtf(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, TAG + " - onDestroy", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
    }

}
