package com.antonageev.weatherapp.models;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.antonageev.weatherapp.IOpenWeatherForecast;
import com.antonageev.weatherapp.IOpenWeatherRequest;
import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.MeasurementsConverter;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.database.City;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.model_forecast.ListWeather;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeModel {

    @Inject
    SharedViewModel model;

    @Inject
    SharedPreferences sharedPreferences;

    private final String TAG = this.getClass().getSimpleName();
    private static final String CITY_TO_SHOW = "cityToShow"; // key to city in sharedPrefs

    private static boolean firstLaunch = true;
    private Parcel localParcel;
    private Consumer<Parcel> modelConsumerParcel;
    private Consumer<WeatherForecast> modelConsumerForecast;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IOpenWeatherRequest openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);
    private IOpenWeatherForecast openWeatherForecast = retrofit.create(IOpenWeatherForecast.class);


    public HomeModel(Consumer<Parcel> consumer, Consumer<WeatherForecast> consumerForecast) {
        modelConsumerParcel = consumer;
        modelConsumerForecast = consumerForecast;
        MainActivity.getViewModelComponent().injectToHomeModel(this); // пришлось привязаться к Activity, т.к. иначе ViewModel не инжектировать.
        getOrInitParcel();
    }

    private void getOrInitParcel() {
        Log.wtf(TAG, "SharedViewModel: Injected model = " + model);
        if (model != null && model.getSavedParcel().getValue() != null) {
            Log.wtf(TAG, "SharedViewModel: " + model.getSavedParcel().getValue());
            updateCurrentWeather(model.getSavedParcel().getValue().getCityData().getCityName());
//            Single.create((SingleOnSubscribe<Parcel>) emitter -> {
//                localParcel = model.getSavedParcel().getValue();
//                if (localParcel == null) return;
//                emitter.onSuccess(localParcel);
//            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(modelConsumerParcel);
        } else if (localParcel == null){
            try {
                String cityToShow = "Moscow"; // default
                if (sharedPreferences != null){
                    cityToShow = sharedPreferences.getString(CITY_TO_SHOW, "Paris");
                }
                updateCurrentWeather(cityToShow);
                Log.w(TAG , "parcel created: " + localParcel);
            } catch (NullPointerException e){
                Log.w(TAG, " Перехват NullPointerException при запуске MainActivity из-за проблем создания parcel");
            }
        }
    }

    public void updateCurrentWeather(String localCityName){

        openWeatherRequest.loadWeather(localCityName, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null){
                            localParcel = new Parcel(WeatherParser.createCityFromWeatherRequest(response.body()));
                            Single.create((SingleOnSubscribe<Parcel>) emitter -> {
//                                localParcel = model.getSavedParcel().getValue();
//                                if (localParcel == null) return;
                                emitter.onSuccess(localParcel);
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(modelConsumerParcel);
                            updateForecast(localCityName);
                        } else {
//                            Snackbar.make(getView(), "trouble with connection to get weather for " + localCityName, BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
//                        Snackbar.make(getView(), "trouble with connection to get weather for " + localCityName, BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateForecast(String localCityName) {

//        String localCity = localParcel.getCityData().getCityName();
//
//        String tempDegrees;
//        if ((sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)).equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
//            tempDegrees = "\u2109"; //F
//        } else {
//            tempDegrees = "\u2103"; //C
//        }

        openWeatherForecast.loadWeather(localCityName, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                        if (response.body() != null) {
                            Single.create((SingleOnSubscribe<WeatherForecast>) emitter -> {
                                emitter.onSuccess(response.body());
                            }).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(modelConsumerForecast);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
//                        Snackbar.make(getView(), getResources().getString(R.string.cityForecastDownloadFailed, localCity), BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }
}
