package com.antonageev.weatherapp.dagger;

import com.antonageev.weatherapp.models.HomeModel;

import dagger.Component;

@Component(modules = {DaggerAppCompatActivityModule.class, DaggerViewModelModule.class, DaggerPreferencesModule.class})
public interface SharedViewModelComponent {
    void injectToHomeModel(HomeModel homeModel);
}
