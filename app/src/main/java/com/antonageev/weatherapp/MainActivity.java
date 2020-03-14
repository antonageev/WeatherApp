package com.antonageev.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textContent;
    private Button buttonToday;
    private Button buttonTomorrow;
    private Button buttonAfterTomorrow;

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
    }

    private void initViews(){
        textContent = findViewById(R.id.textContent);
        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
        buttonAfterTomorrow = findViewById(R.id.buttonAfterTomorrow);
    }

}
