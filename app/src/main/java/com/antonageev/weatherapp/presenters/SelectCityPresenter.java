package com.antonageev.weatherapp.presenters;

import com.antonageev.weatherapp.CityListAdapter;
import com.antonageev.weatherapp.OnCityAdapterItemClickListener;
import com.antonageev.weatherapp.models.SelectCityModel;
import com.antonageev.weatherapp.ui.cities.SelectCityView;

public class SelectCityPresenter extends BasePresenter<SelectCityModel, SelectCityView> {

    public SelectCityPresenter(OnCityAdapterItemClickListener onCityAdapterItemClickListener) {
        setModel(new SelectCityModel(onCityAdapterItemClickListener));
    }

    @Override
    public void updateView() {
        model.updateAdapter();
    }

    public CityListAdapter presenterGetAdapter() {
        return model.getCityListAdapter();
    }

    public void presenterUpdateWeatherData(String cityName) {
        model.updateWeatherData(cityName);
    }

    public void setSelectedCityByPosition(int position) {
        model.setSelectedCity(position);
    }

    public void deleteSelectedCity() {
        model.deleteSelectedCity();
    }

}
