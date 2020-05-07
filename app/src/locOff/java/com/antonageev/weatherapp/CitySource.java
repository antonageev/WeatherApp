package com.antonageev.weatherapp;

import android.util.Log;

import com.antonageev.weatherapp.database.CitiesDao;
import com.antonageev.weatherapp.database.City;

import java.util.List;

public class CitySource {

    private final CitiesDao citiesDao;

    private List<City> cityList;

    public CitySource(CitiesDao citiesDao){
        this.citiesDao = citiesDao;
    }

    public List<City> getCities(){
        if (cityList == null) loadCities();
        return cityList;
    }

    public City getCityByName(String findCity){
        return citiesDao.findCityByName(findCity);
    }

    public void deleteCityByName(String delCity){
        citiesDao.deleteCityByName(delCity);
        loadCities();
        Log.d("CitySource", "deleteCityByName: invoked Dao ");
    }

    private void loadCities(){
        cityList = citiesDao.getAllCities();
    }

    public void addCity(City city){
        citiesDao.InsertCity(city);
        loadCities();
    }

    public void deleteCity(City city){
        citiesDao.deleteCity(city);
        loadCities();
    }

    public void deleteCityById(int idCity){
        citiesDao.deleteCityById(idCity);
    }
}
