package com.antonageev.weatherapp;

import android.app.Application;

import androidx.room.Room;

import com.antonageev.weatherapp.database.CitiesDao;
import com.antonageev.weatherapp.database.CitiesDatabase;

public class App extends Application {

    private static App instance;

    private CitiesDatabase db;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(getApplicationContext(), CitiesDatabase.class, "cities_database")
                .allowMainThreadQueries()
                .build();
    }

    public CitiesDao getCitiesDao(){
        return db.getCitiesDao();
    }
}
