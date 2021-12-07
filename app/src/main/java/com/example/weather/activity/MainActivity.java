package com.example.weather.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.weather.R;
import com.example.weather.adapters.WeatherAdapter;
import com.example.weather.model.VilageFcst;
import com.example.weather.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private VilageFcst weatherModel;
    private WeatherAdapter adapter;
    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new WeatherAdapter();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel.init();
        viewModel.getWeather().observe(this, new Observer<VilageFcst>() {
            @Override
            public void onChanged(VilageFcst vilageFcst) {
                if (vilageFcst != null) {
                    weatherModel = viewModel.getWeather().getValue();

                    adapter.setItems(weatherModel.getResponse().getBody().getItems().getItem());
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}