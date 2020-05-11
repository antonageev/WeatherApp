package com.antonageev.weatherapp.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.antonageev.weatherapp.CircleTransformation;
import com.antonageev.weatherapp.IOpenWeatherForecast;
import com.antonageev.weatherapp.IOpenWeatherRequest;
import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.MapWeatherLinks;
import com.antonageev.weatherapp.MeasurementsConverter;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.WeatherDataLoader;
import com.antonageev.weatherapp.WeatherListAdapter;
import com.antonageev.weatherapp.WeatherParser;
import com.antonageev.weatherapp.model_current.WeatherRequest;
import com.antonageev.weatherapp.model_forecast.ListWeather;
import com.antonageev.weatherapp.model_forecast.WeatherForecast;
import com.antonageev.weatherapp.services.LocationUpdateService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    public static boolean firstLaunch = true;
    private static final String CITY_TO_SHOW = "cityToShow"; // key to city in sharedPrefs
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final String TAG = this.getClass().getSimpleName();

    SharedPreferences sharedPreferences;

    static final String PARCEL = "parcel";

    private TextView city;
    private TextView weatherDescription;
    private TextView temperature;
    private TextView wcf;
    private TextView wind;
    private TextView humidity;
    private Button viewMap;
    private MaterialButton citiesSelect;
    private MaterialButton buttonSettings;
    private ImageView imageView;
    RecyclerView recyclerView;
    WeatherListAdapter weatherListAdapter;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";
    private final String CITY_URL = "cityUrl";

    private int notificationMessageId = 400;

    private BroadcastReceiver forecastBroadcastReceiver;
    private BroadcastReceiver locationBroadcastReceiver;

    private String cityUrl;

    private Parcel localParcel = null;
    private int index = 0;

    private List<Map<String, String>> forecast = new ArrayList<>();

    public HomeFragment(Parcel parcel){
        this.localParcel = parcel;
    }

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item != null ? item.getItemId() : 0;

        if (id == R.id.action_locate){
            requestForPermissions(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestForPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            launchLocationService();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            boolean permit = false;
            for (int gr : grantResults){
                if (gr == PackageManager.PERMISSION_GRANTED){
                    permit = true;
                    break;
                }
            }
            if (permit){
                try {
                    ((NavigationView) requireActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_map).setVisible(true);
                } catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d(TAG, "onRequestPermissionsResult: couldn't make MapItem visible in drawer");
                }
                launchLocationService();
            }
        }
    }

    private void launchLocationService() {
        Intent intent = new Intent(getActivity(), LocationUpdateService.class);
        requireActivity().startService(intent);
    }

    private void getOrInitParcel(Bundle savedInstanceState) {

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Log.wtf(TAG, "SharedViewModel: " + model.getSavedParcel().getValue());
        localParcel = model.getSavedParcel().getValue();

        Log.wtf(TAG, "getOrInit: Parcel: " + localParcel);
        Log.wtf(TAG, "getOrInit: savedInstState: " + savedInstanceState);
        if (localParcel == null && savedInstanceState != null){
            try {
                localParcel = (Parcel) savedInstanceState.getSerializable(PARCEL);
                Log.w(TAG, "parcel savedInstance: " + localParcel);
                Log.w(TAG, "savedInstance: " + savedInstanceState);
            } catch (NullPointerException e){
                Log.w(TAG , " Перехват NullPointerException при запуске MainActivity из-за отсутствия savedInstanceState");
            }
        }

        if (localParcel == null){
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        initListeners();
        getOrInitParcel(savedInstanceState);

        setTextViesFromParcel(localParcel);

        citiesSelect.setVisibility(View.INVISIBLE);
        buttonSettings.setVisibility(View.INVISIBLE);

        initRecyclerView(new ArrayList<Map<String, String>>());

        initBroadcastReceivers();

        updateForecast();

        if (firstLaunch) {
            requestForPermissions(getActivity());
            firstLaunch = false;
        }
    }

    private void initBroadcastReceivers() {
        forecastBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WeatherForecast weatherForecast = (WeatherForecast) intent.getSerializableExtra("cityForecast");
                if (weatherForecast != null) {
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, HH:mm", Locale.getDefault());
                    List<Map<String, String>> resultList = new ArrayList<>();
                    for (ListWeather listWeather : weatherForecast.getListWeather()) {
                        Map<String, String> map = new HashMap<>();
                        map.put("day", format.format(new Date((long) listWeather.getDt() * 1000L)));
                        map.put("weather", listWeather.getWeather()[0].getDescription());
                        map.put("maxTemperature", String.format(Locale.getDefault(), "%.0f", listWeather.getMain().getTempMax()) + " \u2103");
                        resultList.add(map);
                    }
                    weatherListAdapter.weatherListDataChange(resultList);
                }
            }
        };

        locationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String cityToShow = intent.getStringExtra("cityToShow");

                updateCurrentWeather(cityToShow);
                updateForecast();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(locationBroadcastReceiver, new IntentFilter(MainActivity.LOCATION_INTENT_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(locationBroadcastReceiver);
    }

    private void setTextViesFromParcel(Parcel parcel) {
        if (parcel != null){
            String tempDegrees, windUnits;
            if ((sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)).equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
                tempDegrees = "\u2109"; //F
                windUnits = getResources().getString(R.string.windMilesPerHour);
            } else {
                tempDegrees = "\u2103"; //C
                windUnits = getResources().getString(R.string.windMetersPerSecond);
            }

            float localTempMax = MeasurementsConverter.tempFromKelvinToSelectedMeasurement(parcel.getCityData().tempMax, sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));
            float localWcf = MeasurementsConverter.tempFromKelvinToSelectedMeasurement(parcel.getCityData().wcf, sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));
            float localWindSpeed = MeasurementsConverter.windFromMSToSelectedMeasurement(parcel.getCityData().windSpeed, sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC));

            city.setText(parcel.getCityData().cityName);
            weatherDescription.setText(parcel.getCityData().description);
            temperature.setText(String.format(Locale.getDefault(), "%.0f %s", localTempMax, tempDegrees));
            wcf.setText(String.format(Locale.getDefault(),"%s %.0f %s", getString(R.string.feelsLike), localWcf, tempDegrees));
            humidity.setText(String.format(Locale.getDefault(),"%s: %d %s", getResources().getString(R.string.stringHumid),
                    parcel.getCityData().humidity, "%"));
            wind.setText(String.format(getString(R.string.windDirection), assignWindDirection(parcel.getCityData().degrees),
                    String.format(Locale.getDefault(),"%.1f",localWindSpeed), windUnits));


            if (parcel.getCityData().idResponse > 0){
                Picasso.get()
                        .load(MapWeatherLinks.getLinkFromMap(parcel.getCityData().idResponse / 100))
                        .resize(100, 100)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(imageView);
            }
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CITY_TO_SHOW, parcel.getCityData().cityName);
                editor.apply();
            } catch (NullPointerException e){
                Log.w(TAG, "setTextViesFromParcel: requireActivity().getPreferences == NullPointer");
                e.printStackTrace();
            }
        }
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

    private void initRecyclerView(List<Map<String, String>> forecast) {
        LinearLayoutManager ltManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(ltManager);
        weatherListAdapter = new WeatherListAdapter(forecast);
        recyclerView.setAdapter(weatherListAdapter);

        try {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(Objects.requireNonNull(requireActivity().getDrawable(R.drawable.separator)));
            recyclerView.addItemDecoration(itemDecoration);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PARCEL, localParcel);
    }

    private void initViews(View view){
        citiesSelect = view.findViewById(R.id.citiesSelect);
        buttonSettings = view.findViewById(R.id.buttonSettings);
        city = view.findViewById(R.id.city);
        weatherDescription = view.findViewById(R.id.weatherDescription);
        temperature = view.findViewById(R.id.temperature);
        wcf = view.findViewById(R.id.wcf);
        wind = view.findViewById(R.id.wind);
        humidity = view.findViewById(R.id.humidity);
        viewMap = view.findViewById(R.id.viewMap);
        recyclerView = view.findViewById(R.id.recyclerView);
        imageView = view.findViewById(R.id.imageView);
    }

    private void initListeners(){
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_map);
            }
        });
    }

    private Map<String, String> createInitialMapData(){

        Map<String, String> initMap = new HashMap<>();
        initMap.put("index", "0"); // "0" for "Moscow" as default
        initMap.put(CITY, city.getText().toString());
        initMap.put(WEATHER, weatherDescription.getText().toString());
        initMap.put(TEMPERATURE, temperature.getText().toString());
        initMap.put(WIND_CHILL_FACTOR, wcf.getText().toString());
        initMap.put(HUMIDITY, humidity.getText().toString());
        initMap.put(WIND, wind.getText().toString());
        initMap.put(CITY_URL, getResources().getString(R.string.urlMoscow));
        return initMap;
    }

    private void updateCurrentWeather(String localCityName){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IOpenWeatherRequest openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);

        openWeatherRequest.loadWeather(localCityName, WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null){
                            localParcel = new Parcel(WeatherParser.createCityFromWeatherRequest(response.body()));
                            setTextViesFromParcel(localParcel);
                            updateForecast();
                        } else {
                            Snackbar.make(getView(), "trouble with connection to get weather for " + localCityName, BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Snackbar.make(getView(), "trouble with connection to get weather for " + localCityName, BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateForecast() {

        final String localCity;
        if (localParcel != null) {
            localCity = localParcel.getCityData().cityName;
        } else {
            localCity = city.getText().toString();
        }

        String tempDegrees;
        if ((sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)).equals(WeatherDataLoader.MEASURE_IMPERIAL)) {
            tempDegrees = "\u2109"; //F
        } else {
            tempDegrees = "\u2103"; //C
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IOpenWeatherForecast openWeatherForecast = retrofit.create(IOpenWeatherForecast.class);

        openWeatherForecast.loadWeather(localCity, WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                        if (response.body() != null) {
                            SimpleDateFormat formatDay = new SimpleDateFormat("EEEE,", Locale.getDefault());
                            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            List<Map<String, String>> resultList = new ArrayList<>();
                            for (ListWeather listWeather : response.body().getListWeather()) {
                                Map<String, String> map = new HashMap<>();
                                map.put("day", formatDay.format(new Date((long) listWeather.getDt() * 1000L)));
                                map.put("time", formatTime.format(new Date((long) listWeather.getDt() * 1000L)));
                                map.put("weather", listWeather.getWeather()[0].getDescription());
                                map.put("maxTemperature", String.format(Locale.getDefault(), "%.0f",
                                        MeasurementsConverter.tempFromKelvinToSelectedMeasurement(listWeather.getMain().getTempMax(),
                                        sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC) )) +" "+ tempDegrees);
                                resultList.add(map);
                            }
                            if (response.body().getListWeather()[0].getWeather()[0].getId() / 100 == 2) {
                                makeNotification();
                            }
                            weatherListAdapter.weatherListDataChange(resultList);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        Snackbar.make(getView(), getResources().getString(R.string.cityForecastDownloadFailed, localCity), BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.ThunderTitle))
                .setContentText(getString(R.string.within3Hours));
        notificationManager.notify(notificationMessageId++, builder.build());
    }

}