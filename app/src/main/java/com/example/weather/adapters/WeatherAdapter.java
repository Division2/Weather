package com.example.weather.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.VilageFcst;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private List<VilageFcst.Item> items;

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizon_weather_items, parent, false);
        return new WeatherHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {

        Log.e("TAG", getCurrentTime());

        if (items.get(position).getCategory().equals("SKY")) {
            if (items.get(position).getFcstTime().substring(0, 2).equals(getCurrentTime())) {
            }
            holder.txtTime.setText(items.get(position).getFcstTime().substring(0, 2).equals(getCurrentTime()) ? "지금" : items.get(position).getFcstTime().substring(0, 2) + "시");
            holder.ListWeatherImg.setImageResource(R.drawable.ic_launcher_background);
            holder.txtListTemp.setText(items.get(position).getFcstValue());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class WeatherHolder extends RecyclerView.ViewHolder {
        private TextView txtTime;
        private ImageView ListWeatherImg;
        private TextView txtListTemp;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);

            txtTime = (TextView) itemView.findViewById(R.id.txtForeCastTime);
            ListWeatherImg = (ImageView) itemView.findViewById(R.id.imgForeCastWeather);
            txtListTemp = (TextView) itemView.findViewById(R.id.txtForeCastTemp);
        }
    }

    public void setItems(List<VilageFcst.Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    private String getCurrentTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
        return sdf.format(System.currentTimeMillis());
    }
}