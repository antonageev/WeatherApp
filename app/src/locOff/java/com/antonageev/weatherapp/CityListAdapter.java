package com.antonageev.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antonageev.weatherapp.database.City;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder>{

    private CitySource dataSource;
    private OnItemClickListener onItemClickListener;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    private long selectedPosition;

    private String selectedCity;

    public CityListAdapter(CitySource dataSource, Activity activity) {
        this.dataSource = dataSource;
        this.activity = activity;
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public CitySource getDataSource(){
        return dataSource;
    }

    public long getSelectedPosition(){
        return selectedPosition;
    }

    public String getSelectedCity(){
        return selectedCity;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cities, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        List<City> cities = dataSource.getCities();
        City city = cities.get(position);
        holder.getCity().setText(city.cityName);
        holder.getWeather().setText(city.description);
        holder.getTemperature()
                .setText(String.format(Locale.getDefault(), "%.0f %s",MeasurementsConverter.tempFromKelvinToSelectedMeasurement
                        (city.tempMax, sharedPreferences.getString(WeatherDataLoader.KEY_MEASUREMENT, WeatherDataLoader.MEASURE_METRIC)), "\u00b0"));
        holder.getDateTime().setText(new SimpleDateFormat("dd.MM - HH:mm", Locale.getDefault()).format(new Date(city.dateTime)));

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedCity = city.cityName;
                selectedPosition = position;
                return false;
            }
        });

        if (activity != null){
            activity.registerForContextMenu(holder.cardView);
        }


    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.getCities().size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView city;
        private TextView dateTime;
        private TextView weather;
        private TextView temperature;
        View cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            city = cardView.findViewById(R.id.text_view_city);
            dateTime = cardView.findViewById(R.id.text_view_date_time);
            weather = cardView.findViewById(R.id.text_view_weather);
            temperature = cardView.findViewById(R.id.text_view_temperature);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

        }

        TextView getCity() {
            return city;
        }

        TextView getWeather() {
            return weather;
        }

        TextView getTemperature() {
            return temperature;
        }

        TextView getDateTime() {return dateTime;}


    }
}
