package com.antonageev.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder>{

    private List<Map<String, String>> dataSource;
    private OnItemClickListener onItemClickListener;

    public CityListAdapter(List<Map<String, String>> dataSource) {
        this.dataSource = dataSource;
    }

    public void addItem(Map<String, String> elem){
        dataSource.add(0, elem);
        notifyItemInserted(0);
    }

    public List<Map<String, String>> getDataSource(){
        return dataSource;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cities, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        holder.getCity().setText(dataSource.get(position).get("city"));
        holder.getWeather().setText(dataSource.get(position).get("weather"));
        holder.getTemperature().setText(dataSource.get(position).get("temperature"));
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView city;
        private TextView weather;
        private TextView temperature;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.text_view_city);
            weather = itemView.findViewById(R.id.text_view_weather);
            temperature = itemView.findViewById(R.id.text_view_temperature);

            itemView.setOnClickListener(new View.OnClickListener() {
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


    }
}
