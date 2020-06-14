package com.antonageev.weatherapp.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerPreferencesModule {

    @Provides
    SharedPreferences getSharedPreferences(AppCompatActivity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }
}
