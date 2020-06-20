package com.antonageev.weatherapp.models;

import com.antonageev.weatherapp.App;
import com.antonageev.weatherapp.CityListAdapter;
import com.antonageev.weatherapp.CitySource;
import com.antonageev.weatherapp.IOpenWeatherRequestSingle;
import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.OnCityAdapterItemClickListener;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.database.CitiesDao;
import com.antonageev.weatherapp.database.City;
import com.antonageev.weatherapp.model_current.WeatherRequest;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SelectCityModel {

    private CitySource citySource;
    private CityListAdapter cityListAdapter;

    @Inject
    SharedViewModel sharedViewModel;

    @Inject
    IOpenWeatherRequestSingle openWeatherRequestSingle;


    public SelectCityModel(OnCityAdapterItemClickListener onCityAdapterItemClickListener) {
        CitiesDao citiesDao = App.getInstance().getCitiesDao();
        citySource = new CitySource(citiesDao);
        cityListAdapter = new CityListAdapter(null);
        cityListAdapter.setOnCityAdapterItemClickListener(onCityAdapterItemClickListener);
        MainActivity.getViewModelComponent().injectToSelectCityModel(this);
    }

    public CityListAdapter getCityListAdapter() {
        return cityListAdapter;
    }

    public Disposable updateWeatherData(final String city) {

        Single<WeatherRequest> single = openWeatherRequestSingle.loadWeather(city, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY);

        return single.retry(2).subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<WeatherRequest>() {
            @Override
            public void onSuccess(WeatherRequest weatherRequest) {
                if (citySource.getCityByName(city) == null){
                    citySource.addCity(WeatherParser.createCityFromWeatherRequest(weatherRequest));
                    updateAdapter();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public Disposable updateAdapter() {
        return Single.create((SingleOnSubscribe<List<City>>) emitter -> emitter.onSuccess(citySource.getCities()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(cities -> {
                    cityListAdapter.setDataSource(cities);
                    cityListAdapter.notifyDataSetChanged();
                });
    }

    public void setSelectedCity(int position) {
        sharedViewModel.saveParcel(new Parcel(cityListAdapter.getDataSource().get(position)));
    }
    
    public Disposable deleteSelectedCity() {
        return Single.create((SingleOnSubscribe<List<City>>) emitter -> {
            citySource.deleteCityByName(cityListAdapter.getSelectedCity());
            emitter.onSuccess(citySource.getCities());
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(cities -> {
            cityListAdapter.setDataSource(cities);
            cityListAdapter.notifyDataSetChanged();
        });
    }
}
