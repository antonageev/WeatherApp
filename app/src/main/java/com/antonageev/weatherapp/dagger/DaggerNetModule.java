package com.antonageev.weatherapp.dagger;

import com.antonageev.weatherapp.IOpenWeatherRequestSingle;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetModule {

    @Provides
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    IOpenWeatherRequestSingle getIOpenWeatherRequestSingle(Retrofit retrofit) {
        return retrofit.create(IOpenWeatherRequestSingle.class);
    }

}
