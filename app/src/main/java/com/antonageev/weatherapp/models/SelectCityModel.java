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
import com.antonageev.weatherapp.model_current.WeatherRequest;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
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
        cityListAdapter = new CityListAdapter(citySource);
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
                    cityListAdapter.notifyDataSetChanged();
                    // TODO нельзя дергать адаптер из io(), придется делать еще один
                    //  метод для MainThread или прекратить использовать фабрику RXJavaCallAdapterFactory,
                    //  а просто обернуть в Single? Или отвязать адаптер от класса с обращением к БД.
                    //
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });

//        .enqueue(new Callback<WeatherRequest>() {
//                    @Override
//                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
//                        if (response.body() != null) {
//                            if (citySource.getCityByName(city) == null){
//                                citySource.addCity(WeatherParser.createCityFromWeatherRequest(response.body()));
//                                cityListAdapter.notifyDataSetChanged();
//                            } else {
//                                Snackbar.make(getView(), String.format(getString(R.string.cityAlreadyInList), city), BaseTransientBottomBar.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
//
//                    }
//                });
    }

    public void setSelectedCity(int position) {
        sharedViewModel.saveParcel(new Parcel(cityListAdapter.getDataSource().getCities().get(position)));
    }

}
