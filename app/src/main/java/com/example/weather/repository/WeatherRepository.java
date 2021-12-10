package com.example.weather.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.weather.BuildConfig;
import com.example.weather.model.VilageFcst;
import com.example.weather.network.GpsTracker;
import com.example.weather.network.GpsTransfer;
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

    //위치 정보
    private GpsTracker gpsTracker;
    private GpsTransfer gpsTransfer;
    private Context context;

    private SimpleDateFormat sdf;

    private double lat, lng;

    //Default Data
    String serviceKey = BuildConfig.SERVICE_KEY;
    String pageNo = "1";
    String numOfRows = "11";
    String dataType = "JSON";
    String base_date = getTodayDate();
    String base_time = getBaseTime();
    String nx = "37";
    String ny = "127";

    public static WeatherRepository getInstance() {
        if (instance == null) {
            instance = new WeatherRepository();
        }
        return instance;
    }

    public MutableLiveData<VilageFcst> getWeather(Context context) {
        this.context = context;

        //API Instance 생성
        weatherAPI = RetrofitService.getInstance().create(WeatherAPI.class);

        MutableLiveData<VilageFcst> data = new MutableLiveData<>();
        //API 호출
        callWeatherAPI(data);

        return data;
    }

    private void callWeatherAPI(MutableLiveData<VilageFcst> data) {
        //GPS 정보
        gpsTracker = new GpsTracker(context);
        //GPS 위도 경도 -> 격자 좌표 X, Y Convert
        gpsTransfer = new GpsTransfer();

        lat = (int)Math.floor(gpsTracker.getLatitude()) == 0 ? 37.895358333333334 : gpsTracker.getLatitude();
        lng = (int)Math.floor(gpsTracker.getLongitude()) == 0 ? 127.20224444444445 : gpsTracker.getLongitude();

        GpsTransfer.LatXLngY latXLngY = gpsTransfer.convertGRID_GPS(lat, lng);

        nx = String.valueOf((int)latXLngY.x);
        ny = String.valueOf((int)latXLngY.y);

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
        sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return sdf.format(System.currentTimeMillis());
    }

    private String getBaseTime() {
        String basetime;
        sdf = new SimpleDateFormat("HH", Locale.KOREA);
        String nowTime = sdf.format(System.currentTimeMillis())+"00";

        switch(nowTime) {
            case "0200":
            case "0300":
            case "0400":
                basetime = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                basetime = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                basetime = "0800";
                break;
            case "1100":
            case "1200":
            case "1300":
                basetime = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                basetime = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                basetime = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                basetime = "2000";
                break;
            default:
                basetime = "2300";
        }
        return basetime;
    }
}