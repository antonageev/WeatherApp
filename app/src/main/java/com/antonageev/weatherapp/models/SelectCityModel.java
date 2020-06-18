package com.antonageev.weatherapp.models;

import com.antonageev.weatherapp.App;
import com.antonageev.weatherapp.CityListAdapter;
import com.antonageev.weatherapp.CitySource;
import com.antonageev.weatherapp.IOpenWeatherRequest;
import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.OnCityAdapterItemClickListener;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.database.CitiesDao;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectCityModel {

    private CitySource citySource;
    private CityListAdapter cityListAdapter;

    @Inject
    SharedViewModel sharedViewModel;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    private IOpenWeatherRequest openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);


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

    public void updateWeatherData(final String city) {

        openWeatherRequest.loadWeather(city, Locale.getDefault().getCountry(), WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            if (citySource.getCityByName(city) == null){
                                citySource.addCity(WeatherParser.createCityFromWeatherRequest(response.body()));
                                cityListAdapter.notifyDataSetChanged();
                            } else {
//                                Snackbar.make(getView(), String.format(getString(R.string.cityAlreadyInList), city), BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {

                    }
                });
    }

    public void setSelectedCity(int position) {
        sharedViewModel.saveParcel(new Parcel(cityListAdapter.getDataSource().getCities().get(position)));
    }

}
