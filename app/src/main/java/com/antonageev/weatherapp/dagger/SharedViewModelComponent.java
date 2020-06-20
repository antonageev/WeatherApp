package com.antonageev.weatherapp.dagger;

import com.antonageev.weatherapp.CityListAdapter;
import com.antonageev.weatherapp.models.HomeModel;
import com.antonageev.weatherapp.models.SelectCityModel;
import com.antonageev.weatherapp.ui.cities.SelectCityFragment;
import com.antonageev.weatherapp.ui.home.HomeFragment;

import dagger.Component;

@Component(modules = {DaggerAppCompatActivityModule.class, DaggerViewModelModule.class, DaggerPreferencesModule.class, DaggerNetModule.class})
public interface SharedViewModelComponent {
    void injectToHomeModel(HomeModel homeModel);
    void injectToHomeFragment(HomeFragment homeFragment);
    void injectToCityListAdapter(CityListAdapter cityListAdapter);
    void injectToSelectCityFragment(SelectCityFragment selectCityFragment);
    void injectToSelectCityModel(SelectCityModel selectCityModel);
}
