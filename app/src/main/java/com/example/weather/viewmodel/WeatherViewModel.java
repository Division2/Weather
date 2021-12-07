package com.example.weather.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.model.VilageFcst;
import com.example.weather.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<VilageFcst> mutableLiveData;
    private WeatherRepository repository;

    public void init() {
        if (mutableLiveData != null) {
            return;
        }

        repository = WeatherRepository.getInstance();
        mutableLiveData = repository.getWeather();
    }
    public LiveData<VilageFcst> getWeather() {
        return mutableLiveData;
    }
}