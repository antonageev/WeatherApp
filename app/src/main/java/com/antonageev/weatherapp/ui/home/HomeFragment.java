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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.Parcel;
import com.antonageev.weatherapp.PresenterManager;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.WeatherListAdapter;
import com.antonageev.weatherapp.presenters.HomePresenter;
import com.antonageev.weatherapp.services.LocationUpdateService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeView{

    private static boolean firstLaunch = true;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final String TAG = this.getClass().getSimpleName();

    @Inject
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
    private RecyclerView recyclerView;

    private final String CITY = "city";
    private final String TEMPERATURE = "temperature";
    private final String WEATHER = "weather";
    private final String WIND_CHILL_FACTOR = "wcf";
    private final String WIND = "wind";
    private final String HUMIDITY = "humidity";

    private int notificationMessageId = 400;

    private BroadcastReceiver locationBroadcastReceiver;

    private Disposable dAdapter;

    private Consumer<WeatherListAdapter> adapterConsumer = new Consumer<WeatherListAdapter>() {
        @Override
        public void accept(WeatherListAdapter weatherListAdapter) throws Exception {
            recyclerView.setAdapter(weatherListAdapter);
        }
    };

    private HomePresenter homePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.getViewModelComponent().injectToHomeFragment(this);
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
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();

        if (savedInstanceState == null) {
            homePresenter = new HomePresenter(adapterConsumer);
        } else {
            homePresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        citiesSelect.setVisibility(View.INVISIBLE);
        buttonSettings.setVisibility(View.INVISIBLE);

        initRecyclerView();

        initBroadcastReceivers();

        if (firstLaunch) {
            requestForPermissions(getActivity());
            firstLaunch = false;
        }
    }



    private void initBroadcastReceivers() {
        locationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String cityToShow = intent.getStringExtra("cityToShow");
                homePresenter.presenterUpdateCurrentWeather(cityToShow);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(locationBroadcastReceiver, new IntentFilter(MainActivity.LOCATION_INTENT_FILTER));
        homePresenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(locationBroadcastReceiver);
        homePresenter.unbindView(this);
        if (dAdapter != null && !dAdapter.isDisposed()) dAdapter.dispose();
    }

    @Override
    public void setTextViesFromMap(Map<String, String> weatherMap) {
            city.setText(weatherMap.get(CITY));
            weatherDescription.setText(weatherMap.get(WEATHER));
            temperature.setText(weatherMap.get(TEMPERATURE));
            wcf.setText(weatherMap.get(WIND_CHILL_FACTOR));
            humidity.setText(weatherMap.get(HUMIDITY));
            wind.setText(weatherMap.get(WIND));
    }

    private void initRecyclerView() {
        LinearLayoutManager ltManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(ltManager);

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
        PresenterManager.getInstance().savePresenter(homePresenter, outState);
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
        viewMap.setVisibility(View.GONE); // hide button - no need to duplicate functionality
        recyclerView = view.findViewById(R.id.recyclerView);
        imageView = view.findViewById(R.id.imageView);
    }

    private void initListeners(){
        viewMap.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_map));
    }

    //TODO поставить нотификацию
    private void makeNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.ThunderTitle))
                .setContentText(getString(R.string.within3Hours));
        notificationManager.notify(notificationMessageId++, builder.build());
    }

}