package com.antonageev.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_CITIES = 1;


    private final String TAG = this.getClass().getSimpleName();

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
    private Intent intentToCitiesSelect;
    private Intent intentToSettings;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private String cityUrl;


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
        Toast.makeText(this, TAG + " " + savedInstance + " - onCreate", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate()");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE_CITIES) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == RESULT_OK){
            try {
                city.setText(data.getStringExtra("city"));
                weatherDescription.setText(data.getStringExtra("weather"));
                temperature.setText(data.getStringExtra("temperature"));
                wcf.setText(data.getStringExtra("wcf"));
                humidity.setText(data.getStringExtra("humidity"));
                wind.setText(data.getStringExtra("wind"));
                cityUrl = data.getStringExtra("cityUrl");
            } catch (NullPointerException e){
                Log.e(TAG, "Null pointer exception while receiving data from CitySelect Activity");
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, TAG +" - onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, TAG + " - onSaveInstanceState", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSaveInstanceState()");
        outState.putString(CITY, city.getText().toString());
        outState.putString(TEMPERATURE, temperature.getText().toString());
        outState.putString(WEATHER, weatherDescription.getText().toString());
        outState.putString(WIND_CHILL_FACTOR, wcf.getText().toString());
        outState.putString(WIND, wind.getText().toString());
        outState.putString(HUMIDITY, humidity.getText().toString());
        outState.putString(CITY_URL, cityUrl);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this, TAG + " - onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestoreInstanceState()");
        city.setText(savedInstanceState.getString(CITY));
        temperature.setText(savedInstanceState.getString(TEMPERATURE));
        weatherDescription.setText(savedInstanceState.getString(WEATHER));
        wcf.setText(savedInstanceState.getString(WIND_CHILL_FACTOR));
        wind.setText(savedInstanceState.getString(WIND));
        humidity.setText(savedInstanceState.getString(HUMIDITY));
        cityUrl = savedInstanceState.getString(CITY_URL);
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
                intentToSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentToSettings);
            }
        });
        citiesSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCitiesSelect = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivityForResult(intentToCitiesSelect, REQUEST_CODE_CITIES);
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

    private void initViews(){
        textContent = findViewById(R.id.textContent);
        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
        buttonAfterTomorrow = findViewById(R.id.buttonAfterTomorrow);
        citiesSelect = findViewById(R.id.citiesSelect);
        buttonSettings = findViewById(R.id.buttonSettings);
        city = findViewById(R.id.city);
        weatherDescription = findViewById(R.id.weatherDescription);
        temperature = findViewById(R.id.temperature);
        wcf = findViewById(R.id.wcf);
        wind = findViewById(R.id.wind);
        humidity = findViewById(R.id.humidity);
        aboutCity = findViewById(R.id.aboutCity);
        cityUrl = getResources().getString(R.string.urlMoscow); //default
    }

}
