package com.example.weather.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.R;
import com.example.weather.model.VilageFcst;
import com.example.weather.network.GpsTracker;
import com.example.weather.viewmodel.WeatherViewModel;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

//    private RecyclerView recyclerView;
//    private WeatherAdapter adapter;
    private WeatherViewModel viewModel;
    private VilageFcst weatherModel;
    private GpsTracker gpsTracker;
    private Geocoder geocoder;

    private TextView txtCity, txtTemp, txtStatus, txtHumidity;
    private ImageView imgWeather;

    private String sky_value, pty_value, temp, humidity;
    private int fcstTime;
    List<Address> address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        txtStatus = findViewById(R.id.txtStatus);
        txtHumidity = findViewById(R.id.txtHumidity);
        imgWeather = findViewById(R.id.imgWeather);

        gpsTracker = new GpsTracker(this);
        geocoder = new Geocoder(this);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel.init(this);
        viewModel.getWeather().observe(this, new Observer<VilageFcst>() {
            @Override
            public void onChanged(VilageFcst vilageFcst) {
                if (vilageFcst != null) {
                    weatherModel = viewModel.getWeather().getValue();

                    /*
                        SKY : 하늘상태
                        PTY : 강수형태
                        TMP : 1시간 기온
                     */
                    sky_value = weatherModel.getResponse().getBody().getItems().getItem().get(5).getFcstValue();                 //Category : SKY
                    pty_value = weatherModel.getResponse().getBody().getItems().getItem().get(6).getFcstValue();                 //Category : PTY
                    fcstTime = Integer.parseInt(weatherModel.getResponse().getBody().getItems().getItem().get(0).getFcstTime()); //Category : TMP
                    temp = weatherModel.getResponse().getBody().getItems().getItem().get(0).getFcstValue();                      //Category : TMP
                    humidity = weatherModel.getResponse().getBody().getItems().getItem().get(10).getFcstValue();                 //Category : REH

                    //Geocoder를 이용한 위도 경도로 주소 찾기
                    try {
                        address = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);

                        if (address != null) {
                            if (address.size() != 0) {
                                //City
                                txtCity.setText(address.get(0).getThoroughfare());
                            }
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Temp
                    txtTemp.setText(temp + "˚");
                    //Humidity
                    txtHumidity.setText("습도 : " + humidity + "%");

                    //Weather Image & Status
                    switch (pty_value) {
                        //특수 상황 없음
                        case "0":
                            //SKY 0 : 맑음
                            if (sky_value.equals("1")) {
                                txtStatus.setText("맑음");
                                //예보 기준이 낮인지 밤인지
                                if (1759 >= fcstTime) {
                                    imgWeather.setImageResource(R.drawable.sunny_day);
                                }
                                else {
                                    imgWeather.setImageResource(R.drawable.sunny_night);
                                }
                            }
                            //SKY 3 : 구름 많음
                            else if (sky_value.equals("3")) {
                                txtStatus.setText("구름 많음");
                                //예보 기준이 낮인지 밤인지
                                if (1759 >= fcstTime) {
                                    imgWeather.setImageResource(R.drawable.cloudy_day);
                                }
                                else {
                                    imgWeather.setImageResource(R.drawable.cloudy_night);
                                }
                            }
                            //SKY 4 : 흐림
                            else if (sky_value.equals("4")) {
                                txtStatus.setText("흐림");
                                imgWeather.setImageResource(R.drawable.cloudy);
                            }
                            break;
                        //비
                        case "1":
                            txtStatus.setText("비");
                            imgWeather.setImageResource(R.drawable.rain);
                            break;
                        //눈
                        case "3":
                            txtStatus.setText("눈");
                            imgWeather.setImageResource(R.drawable.snow);
                            break;
                        //소나기
                        case "4":
                            txtStatus.setText("소나기");
                            imgWeather.setImageResource(R.drawable.shower);
                            break;
                        default:
                            imgWeather.setImageResource(R.drawable.thunder);
                            break;
                    }

//                    adapter.setItems(weatherModel.getResponse().getBody().getItems().getItem());
//                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}