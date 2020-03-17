package com.antonageev.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private TextView textContent;
    private Button buttonToday;
    private Button buttonTomorrow;
    private Button buttonAfterTomorrow;
    private ImageButton citiesSelect;
    private ImageButton buttonSettings;
    private Intent intentToCitiesSelect;
    private Intent intentToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setButtonListeners();
        String savedInstance;
        if (savedInstanceState == null){
            savedInstance = "First launch";
        } else {
            savedInstance = "Repeated launch";
        }
        Toast.makeText(this, "MainActivity " + savedInstance +" - onCreate", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "MainActivity - onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, "MainActivity - onSaveInstanceState", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "MainActivity " + savedInstance + "- onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestoreInstanceState()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "MainActivity - onRestart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "MainActivity - onResume", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "MainActivity - onPause", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "MainActivity - onStop", Toast.LENGTH_SHORT).show();
        Log.wtf(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MainActivity - onDestroy", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
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
                Toast.makeText(MainActivity.this, R.string.stringContentTom, Toast.LENGTH_SHORT).show();
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
                startActivity(intentToSettings);
            }
        });
        citiesSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentToCitiesSelect);
            }
        });
    }

    private void initViews(){
        textContent = findViewById(R.id.textContent);
        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
        buttonAfterTomorrow = findViewById(R.id.buttonAfterTomorrow);
        citiesSelect = findViewById(R.id.citiesSelect);
        buttonSettings = findViewById(R.id.buttonSettings);
        intentToCitiesSelect = new Intent(this, SelectCityActivity.class);
        intentToSettings = new Intent(this, SettingsActivity.class);
    }

}
