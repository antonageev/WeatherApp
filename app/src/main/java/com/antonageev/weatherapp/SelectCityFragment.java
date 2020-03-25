package com.antonageev.weatherapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.antonageev.weatherapp.MainFragment.PARCEL;
/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment {

    Parcel currentParcel;
    private Button backButton;
    private ListView listView;
    private List<Map<String, String>> citiesWeatherList;

    private boolean mDualPane;

    public static SelectCityFragment create(int index){

        SelectCityFragment selectCityFragment = new SelectCityFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        selectCityFragment.setArguments(args);
        return selectCityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        citiesWeatherList = initHardCodedCitiesList();
        initViews(view);
        setListeners();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("currentCity", currentParcel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        if (savedInstanceState != null){
            currentParcel = (Parcel) savedInstanceState.getSerializable("currentCity");
        } else {
            currentParcel = new Parcel(citiesWeatherList.get(0));
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), citiesWeatherList,
                R.layout.list_item_cities,
                new String[]{"city", "weather", "temperature"},
                new int[]{R.id.text_view_city, R.id.text_view_weather, R.id.text_view_temperature});
        listView.setAdapter(adapter);

        if (mDualPane){
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showMainFragment(currentParcel);
            Log.w("Show main fragment", "Launched");
        }
    }

    private void showMainFragment(Parcel parcel){
        Log.wtf("showMainF : mDualPane", String.valueOf(mDualPane));
        if (mDualPane){
            //MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.mainFragmentLayout);

            //if (mainFragment == null){
                MainFragment mainFragment = MainFragment.create(parcel);
                Log.wtf("mainFragment", String.valueOf(mainFragment));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragmentLayout, mainFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            //}
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MainActivity.class);
            intent.putExtra(PARCEL, parcel);
            startActivity(intent);
        }
    }

    private void initViews(View view){
        backButton = view.findViewById(R.id.backButton);
        listView = view.findViewById(R.id.listCities);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> rowData = (Map<String, String>) parent.getItemAtPosition(position);
                Parcel parcel = new Parcel(rowData);
//                Intent intentResult = new Intent();
//                intentResult.putExtra("city", rowData.get("city"));
//                intentResult.putExtra("weather", rowData.get("weather"));
//                intentResult.putExtra("temperature", rowData.get("temperature"));
//                intentResult.putExtra("wcf", rowData.get("wcf"));
//                intentResult.putExtra("humidity", rowData.get("humidity"));
//                intentResult.putExtra("wind", rowData.get("wind"));
//                intentResult.putExtra("cityUrl", rowData.get("cityUrl"));
                showMainFragment(parcel);
//                setResult(RESULT_OK, intentResult);
//                finish();
            }
        });
    }

    private List<Map<String, String>> initHardCodedCitiesList() {
        List<Map<String, String>> citiesList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("city", "NewYork");
        map.put("weather", "Cloudy");
        map.put("temperature", "13 C");
        map.put("wcf", "5 C");
        map.put("humidity", "Humidity: 65%");
        map.put("wind", "Wind: SW, 3 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlNewYork));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("city", "Tokyo");
        map.put("weather", "Shiny");
        map.put("temperature", "8 C");
        map.put("wcf", "0 C");
        map.put("humidity", "Humidity: 50%");
        map.put("wind", "Wind: E, 1 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlTokyo));
        citiesList.add(map);

        map = new HashMap<>();
        map.put("city", "Moscow");
        map.put("weather", "Rainy");
        map.put("temperature", "6 C");
        map.put("wcf", "-2 C");
        map.put("humidity", "Humidity: 90%");
        map.put("wind", "Wind: N, 8 m/s");
        map.put("cityUrl", getResources().getString(R.string.urlMoscow));
        citiesList.add(map);

        return citiesList;
    }
}
