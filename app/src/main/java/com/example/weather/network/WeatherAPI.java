package com.example.weather.network;

import com.example.weather.model.VilageFcst;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("getVilageFcst")
    Call<VilageFcst> getVilageFcst (
            @Query("ServiceKey") String serviceKey,
            @Query("pageNo") String pageNo,
            @Query("numOfRows") String numOfRows,
            @Query("dataType") String dataType,
            @Query("base_date") String base_date,
            @Query("base_time") String base_time,
            @Query("nx") String nx,
            @Query("ny") String ny);
}