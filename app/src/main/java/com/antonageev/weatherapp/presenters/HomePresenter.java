package com.antonageev.weatherapp.presenters;

import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.antonageev.weatherapp.models.HomeModel;
import com.antonageev.weatherapp.ui.home.HomeView;

import io.reactivex.functions.Consumer;

public class HomePresenter extends BasePresenter<HomeModel, HomeView> {

    private Consumer<Parcel> consumerParcel;
    private Consumer<WeatherForecast> consumerForecast;

    public HomePresenter() {
        consumerParcel = parcel -> {
            if (view != null){
                view.setTextViesFromParcel(parcel);
            }
        };
        consumerForecast = weatherForecast -> {
            if (view != null) {
                view.updateForecastList(weatherForecast);
            }
        };
    }

    @Override
    public void updateView() {
        model.getOrInitParcel();
    }

    @Override
    public void bindView(HomeView view) {
        super.bindView(view);
        if (model == null) {
            loadData();
        }
    }

    private void loadData() {
        setModel(new HomeModel(consumerParcel, consumerForecast));
    }

    public void presenterUpdateCurrentWeather(String cityName) {
        model.updateCurrentWeather(cityName);
    }

}
