package com.antonageev.weatherapp.presenters;

import com.antonageev.weatherapp.WeatherListAdapter;
import com.antonageev.weatherapp.models.HomeModel;
import com.antonageev.weatherapp.ui.home.HomeView;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class HomePresenter extends BasePresenter<HomeModel, HomeView> {

    private Consumer<Map<String, String>> consumerMap;
    private Consumer<WeatherListAdapter> presenterAdapterConsumer;

    public HomePresenter(Consumer<WeatherListAdapter> adapterConsumer) {
        consumerMap = resultMap -> {
            if (view != null){
                view.setTextViesFromMap(resultMap);
            }
        };
        presenterAdapterConsumer = adapterConsumer;
    }

    @Override
    public void updateView() {
        model.getOrInitParcel();
        model.updateAdapter();
    }

    @Override
    public void bindView(HomeView view) {
        super.bindView(view);
        if (model == null) {
            loadData();
        }
    }

    private void loadData() {
        setModel(new HomeModel(consumerMap, presenterAdapterConsumer));
    }

    public void presenterUpdateCurrentWeather(String cityName) {
        model.updateCurrentWeather(cityName);
    }

}
