package com.antonageev.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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
