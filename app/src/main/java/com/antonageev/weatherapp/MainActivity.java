package com.antonageev.weatherapp;


import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.antonageev.weatherapp.observer.Observer;
import com.antonageev.weatherapp.observer.Publisher;
import com.antonageev.weatherapp.observer.PublisherGetter;

public class MainActivity extends AppCompatActivity implements PublisherGetter {

    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Main Activity onCreate()", Toast.LENGTH_SHORT).show();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Fragment selectCityFragment = getSupportFragmentManager().findFragmentById(R.id.selectCityFragment);
            Fragment mainFragment = getSupportFragmentManager().findFragmentById(R.id.mainFragment);
            publisher.subscribe((Observer)mainFragment);
        }

    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }
}
