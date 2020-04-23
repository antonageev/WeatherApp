package com.antonageev.weatherapp.ui.cities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.antonageev.weatherapp.CityListAdapter;
import com.antonageev.weatherapp.IOpenWeatherRequest;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherData;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment {

    private static final String PARCEL = "parcel";
    private final String TAG = this.getClass().getSimpleName();
    private final Handler handler = new Handler();

    Parcel currentParcel;
    private MaterialButton findButton;
    private TextInputEditText editTextCity;
    private CitiesWeatherList citiesWeatherList;
    private RecyclerView recyclerView;

    private CityListAdapter cityListAdapter;

    private WeatherData resultData;

    private DialogCustomFragment dlgCustom;

    private JSONObject weatherJSONData;

    private boolean mDualPane;

    SharedViewModel sharedViewModel;

    public static SelectCityFragment create(int index){
        SelectCityFragment selectCityFragment = new SelectCityFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        selectCityFragment.setArguments(args);
        return selectCityFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dlgCustom = new DialogCustomFragment();

        if (sharedViewModel == null) {
            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        }
        citiesWeatherList = sharedViewModel.getStoredCitiesWeatherList().getValue();
        if (citiesWeatherList == null){
            citiesWeatherList = new CitiesWeatherList(new ArrayList<Map<String, String>>());
        }
        initViews(view);
        setListeners();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (sharedViewModel == null){
            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        }
        sharedViewModel.saveCitiesWeatherList(citiesWeatherList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        initRecyclerView();

    }

    private void initRecyclerView() {
        cityListAdapter = new CityListAdapter(citiesWeatherList.getList());
        LinearLayoutManager lt = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(lt);
        recyclerView.setAdapter(cityListAdapter);
        cityListAdapter.setOnItemClickListener(new CityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Snackbar.make(view, getResources().getString(R.string.snackBarSure), BaseTransientBottomBar.LENGTH_SHORT).
                        setAction(getResources().getString(R.string.confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentParcel = new Parcel(cityListAdapter.getDataSource().get(position));
                                showMainFragment(currentParcel);
                            }
                        }).show();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(requireActivity().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void showMainFragment(Parcel parcel) {
        if (sharedViewModel == null){
            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        }
        sharedViewModel.saveParcel(parcel);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
    }

    private void initViews(View view){
        findButton = view.findViewById(R.id.findButton);
        editTextCity = view.findViewById(R.id.editTextCity);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setListeners(){
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeatherData(editTextCity.getText().toString());
            }
        });
    }

    public void onDialogResult(String resultDialog){
        Toast.makeText(getActivity(),"selected button: " + resultDialog, Toast.LENGTH_SHORT).show();
    }

    private void updateWeatherData(final String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IOpenWeatherRequest openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);

        openWeatherRequest.loadWeather(city, WeatherDataLoader.UNITS_METRIC, WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            if (citiesWeatherList.isCityInList(city)) {
                                Snackbar.make(getView(), "City " + city + " already in list", BaseTransientBottomBar.LENGTH_SHORT).show();
                            } else {
                                cityListAdapter.addItem(renderWeather(response.body()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                            dlgCustom.show(getParentFragmentManager(), "");
                            editTextCity.requestFocus();
                    }
                });

        //***


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                weatherJSONData = WeatherDataLoader.getJSONdata(city, WeatherDataLoader.WEATHER_CURRENT_DATA);
//                if (weatherJSONData != null){
//                    final WeatherRequest weatherRequest = (WeatherRequest) WeatherParser.renderWeatherData(weatherJSONData, WeatherDataLoader.WEATHER_CURRENT_DATA);
////                    final WeatherRequest weatherRequest = gson.fromJson(String.valueOf(weatherJSONdata), WeatherRequest.class);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (citiesWeatherList.isCityInList(city)){
//                                Snackbar.make(getView(),"City "+ city + " already in list", BaseTransientBottomBar.LENGTH_SHORT).show();
//                            } else {
//                                cityListAdapter.addItem(renderWeather(weatherRequest));
//                            }
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dlgCustom.show(getParentFragmentManager(), "");
//                            editTextCity.requestFocus();
//                        }
//                    });
//                    Log.e(TAG, "Connection failed");
//                }
//            }
//        }).start();
    }

    private Map<String, String> renderWeather(WeatherRequest weatherRequest){
        Map<String, String> localMap = new HashMap<>();
        localMap.put("index", "0");
        localMap.put("city", weatherRequest.getName());
        localMap.put("weather", weatherRequest.getWeather()[0].getMain());
        localMap.put("temperature", String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp()) + " \u2103");
        localMap.put("wcf",  String.format(Locale.getDefault(),"%.0f",weatherRequest.getMain().getFeelsLike()) + " \u2103");
        localMap.put("humidity", "Humidity: " + weatherRequest.getMain().getHumidity()+"%");
        localMap.put("wind", String.format("Wind: %s, %s m/s", assignWindDirection(weatherRequest.getWind().getDeg()),
                String.format(Locale.getDefault(),"%.1f",weatherRequest.getWind().getSpeed())));
        localMap.put("cityUrl", getResources().getString(R.string.urlMoscow));
        localMap.put("id", String.valueOf(weatherRequest.getWeather()[0].getId()));

        Log.d(TAG, "renderWeather: getWeather()[0].getId(): " + weatherRequest.getWeather()[0].getId());

        return localMap;
    }

    private String assignWindDirection(int degrees){
        if (degrees > 22 && degrees <= 67) return getResources().getStringArray(R.array.windDirection)[0];
        if (degrees > 68 && degrees <= 112) return getResources().getStringArray(R.array.windDirection)[1];
        if (degrees > 112 && degrees <= 157) return getResources().getStringArray(R.array.windDirection)[2];
        if (degrees > 157 && degrees <= 202) return getResources().getStringArray(R.array.windDirection)[3];
        if (degrees > 202 && degrees <= 247) return getResources().getStringArray(R.array.windDirection)[4];
        if (degrees > 247 && degrees <= 292) return getResources().getStringArray(R.array.windDirection)[5];
        if (degrees > 292 && degrees <= 337) return getResources().getStringArray(R.array.windDirection)[6];
        if (degrees > 337 || degrees <= 22) return getResources().getStringArray(R.array.windDirection)[7];
        return "unknown";
    }


    private List<Map<String, String>> initCitiesList() {
        List<Map<String, String>> citiesList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("index", "0");
        map.put("city", "Moscow");
        map.put("weather", "Rainy");
        map.put("temperature", "6 C");
        map.put("wcf", "-2 C");
        map.put("humidity", "Humidity: 90%");
        map.put("wind", "Wind: N, 8 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlMoscow));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("index", "1");
        map.put("city", "NewYork");
        map.put("weather", "Cloudy");
        map.put("temperature", "13 C");
        map.put("wcf", "5 C");
        map.put("humidity", "Humidity: 65%");
        map.put("wind", "Wind: SW, 3 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlNewYork));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("index", "2");
        map.put("city", "Tokyo");
        map.put("weather", "Shiny");
        map.put("temperature", "8 C");
        map.put("wcf", "0 C");
        map.put("humidity", "Humidity: 50%");
        map.put("wind", "Wind: E, 1 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlTokyo));
        citiesList.add(map);

        return citiesList;
    }
}
