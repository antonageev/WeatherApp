package com.antonageev.weatherapp.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerAppCompatActivityModule {

    AppCompatActivity activity;

    public DaggerAppCompatActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    AppCompatActivity getActivity() {
        return activity;
    }
}
