package com.antonageev.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private List<Map<String, String>> dataSource;

    public WeatherListAdapter(List<Map<String, String>> dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_weather, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherListAdapter.ViewHolder holder, int position) {
        holder.getTextViewDay().setText(dataSource.get(position).get("day"));
        holder.getTextViewWeather().setText(dataSource.get(position).get("weather"));
        holder.getTextViewMaxTemperature().setText(dataSource.get(position).get("maxTemperature"));
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDay;
        private TextView textViewWeather;
        private TextView textViewMaxTemperature;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.day);
            textViewWeather = itemView.findViewById(R.id.weather);
            textViewMaxTemperature = itemView.findViewById(R.id.maxTemperature);
        }

        TextView getTextViewDay() {
            return textViewDay;
        }

        TextView getTextViewWeather() {
            return textViewWeather;
        }

        TextView getTextViewMaxTemperature() {
            return textViewMaxTemperature;
        }
    }
}
