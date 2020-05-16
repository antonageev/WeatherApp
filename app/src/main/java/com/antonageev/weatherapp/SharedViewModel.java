package com.antonageev.weatherapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.antonageev.weatherapp.ui.cities.CitiesWeatherList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Parcel> selectedParcel = new MutableLiveData<>();
    private MutableLiveData<CitiesWeatherList> storedCitiesWeatherList = new MutableLiveData<>();

    public void saveParcel(Parcel parcel){
        selectedParcel.setValue(parcel);
    }

    public LiveData<Parcel> getSavedParcel(){
        return selectedParcel;
    }

    public void saveCitiesWeatherList (CitiesWeatherList citiesWeatherList){
        storedCitiesWeatherList.setValue(citiesWeatherList);
    }

    public LiveData<CitiesWeatherList> getStoredCitiesWeatherList(){
        return storedCitiesWeatherList;
    }

}
