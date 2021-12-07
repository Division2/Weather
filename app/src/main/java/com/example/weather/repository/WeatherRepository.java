package com.example.weather.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.weather.BuildConfig;
import com.example.weather.model.VilageFcst;
import com.example.weather.network.WeatherAPI;
import com.example.weather.network.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static WeatherRepository instance = null;

    private WeatherAPI weatherAPI;

    String serviceKey = BuildConfig.SERVICE_KEY;
    String pageNo = "1";
    String numOfRows = "1000";
    String dataType = "JSON";
    String base_date = getTodayDate();
    String base_time = "0500";
    String nx = "55";
    String ny = "127";

    public static WeatherRepository getInstance() {
        if (instance == null) {
            instance = new WeatherRepository();
        }
        return instance;
    }

    public MutableLiveData<VilageFcst> getWeather() {
        weatherAPI = RetrofitService.getInstance().create(WeatherAPI.class);

        MutableLiveData<VilageFcst> data = new MutableLiveData<>();
        callWeatherAPI(data);

        Log.e("TAG", "getWeather");

        return data;
    }

    private void callWeatherAPI(MutableLiveData<VilageFcst> data) {
        weatherAPI.getVilageFcst(serviceKey, pageNo, numOfRows, dataType, base_date, base_time, nx, ny).enqueue(new Callback<VilageFcst>() {
            @Override
            public void onResponse(Call<VilageFcst> call, Response<VilageFcst> response) {
                if (response.isSuccessful()) {
                    VilageFcst vilageFcst = response.body();

                    data.postValue(vilageFcst);
                }
            }

            @Override
            public void onFailure(Call<VilageFcst> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return sdf.format(System.currentTimeMillis());
    }
}