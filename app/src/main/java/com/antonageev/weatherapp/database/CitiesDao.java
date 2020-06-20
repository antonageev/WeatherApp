package com.antonageev.weatherapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertCity(City city);

    @Update
    void updateCity(City city);

    @Delete
    void deleteCity(City city);

    @Query("SELECT * FROM city WHERE city_name = :findCity")
    City findCityByName(String findCity);

    @Query("DELETE FROM city WHERE idCity = :idCity")
    void deleteCityById(long idCity);

    @Query("DELETE FROM city WHERE city_name = :delCityName")
    void deleteCityByName(String delCityName);

    @Query("SELECT * FROM city")
    List<City> getAllCities();

    @Query("SELECT * FROM city ORDER BY dateTime DESC LIMIT 1")
    City getLastAddedCity();

    @Query("SELECT COUNT() FROM city")
    long getCountCities();
}
