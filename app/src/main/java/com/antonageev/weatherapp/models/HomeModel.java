package com.antonageev.weatherapp.models;

import android.content.SharedPreferences;
import android.util.Log;

import com.antonageev.weatherapp.IOpenWeatherForecast;
import com.antonageev.weatherapp.IOpenWeatherRequest;
import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeModel {

    @Inject
    SharedViewModel model;

    @Inject
    SharedPreferences sharedPreferences;

    private final String TAG = this.getClass().getSimpleName();
    private static final String CITY_TO_SHOW = "cityToShow"; // key to city in sharedPrefs

    private Parcel localParcel;
    private Consumer<Parcel> modelConsumerParcel;
    private Consumer<WeatherForecast> modelConsumerForecast;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IOpenWeatherRequest openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);
    private IOpenWeatherForecast openWeatherForecast = retrofit.create(IOpenWeatherForecast.class);


    public HomeModel(Consumer<Parcel> consumerParcel , Consumer<WeatherForecast> consumerForecast) {
        modelConsumerParcel = consumerParcel;
        modelConsumerForecast = consumerForecast;
        MainActivity.getViewModelComponent().injectToHomeModel(this); // пришлось привязаться к Activity, т.к. иначе ViewModel не инжектировать.
    }

    public void getOrInitParcel() {
        Log.wtf(TAG, "SharedViewModel: Injected model = " + model);
        if (model != null && model.getSavedParcel().getValue() != null) {
            Log.wtf(TAG, "SharedViewModel: " + model.getSavedParcel().getValue());
            updateCurrentWeather(model.getSavedParcel().getValue().getCityData().getCityName());
        } else if (localParcel == null) {
            try {
                String cityToShow = "Moscow"; // default
                if (sharedPreferences != null) {
                    cityToShow = sharedPreferences.getString(CITY_TO_SHOW, "Paris");
                }
                updateCurrentWeather(cityToShow);
                Log.w(TAG, "parcel created: " + localParcel);
            } catch (NullPointerException e) {
                Log.w(TAG, " Перехват NullPointerException при запуске MainActivity из-за проблем создания parcel");
            }
        } else {
            updateCurrentWeather(localParcel.getCityData().getCityName());
        }
    }

    public Disposable updateCurrentWeather(String localCityName) {
        return Single.create((SingleOnSubscribe<Parcel>) emitter -> {
            openWeatherRequest.loadWeather(localCityName, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                            if (response.body() != null) {
                                localParcel = new Parcel(WeatherParser.createCityFromWeatherRequest(response.body()));
                                emitter.onSuccess(localParcel);
                                updateForecast(localCityName);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherRequest> call, Throwable t) {
                            emitter.onError(t);
                        }
                    });
        }).retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(modelConsumerParcel);

    }

    private Disposable updateForecast(String localCityName) {
        return Single.create((SingleOnSubscribe<WeatherForecast>) emitter -> {
            openWeatherForecast.loadWeather(localCityName, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY)
                    .enqueue(new Callback<WeatherForecast>() {
                        @Override
                        public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                            if (response.body() != null) {
                                emitter.onSuccess(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherForecast> call, Throwable t) {
                            emitter.onError(t);
                        }
                    });
        }).retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(modelConsumerForecast);
    }
}
