package com.antonageev.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Button backButton;
    private Switch switchDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        String savedInstance;
        if (savedInstanceState == null){
            savedInstance = "NO saved data";
        } else {
            savedInstance = "Saved data EXISTS";
        }
        Toast.makeText(this, TAG + " " + savedInstance + "- onRestoreInstanceState", Toast.LENGTH_SHORT).show();
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

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        switchDarkTheme = findViewById(R.id.switchDarkTheme);
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
                if (switchDarkTheme.isChecked()){
                    switchDarkTheme.setText(R.string.textSwitchDarkThemeOn);
                } else {
                    switchDarkTheme.setText(R.string.textSwitchDarkThemeOff);
                }
            }
        });
    }
}
