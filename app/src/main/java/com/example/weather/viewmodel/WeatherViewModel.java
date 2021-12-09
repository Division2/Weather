package com.example.weather.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.model.VilageFcst;
import com.example.weather.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<VilageFcst> mutableLiveData;
    private WeatherRepository repository;

    public void init(Context context) {
        if (mutableLiveData != null) {
            return;
        }

        repository = WeatherRepository.getInstance();
        mutableLiveData = repository.getWeather(context);
    }
    public LiveData<VilageFcst> getWeather() {
        return mutableLiveData;
    }
}