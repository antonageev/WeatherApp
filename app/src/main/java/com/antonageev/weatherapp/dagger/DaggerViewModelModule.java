package com.antonageev.weatherapp.dagger;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.antonageev.weatherapp.SharedViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerViewModelModule {

    @Provides
    ViewModelProvider getViewModelProvider(AppCompatActivity activity) {
        return new ViewModelProvider(activity);
    }

    @Provides
    SharedViewModel getSharedViewModel(ViewModelProvider provider) {
        return provider.get(SharedViewModel.class);
    }

}
