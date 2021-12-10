package com.example.weather.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    //실시간 날씨 정보
    private TextView txtCity, txtTemp, txtStatus;
    private ImageView imgWeather;
    private String sky_value, pty_value, temp;
    private int fcstTime;
    List<Address> address;

    //강수량, 습도, 풍향, 풍속
    private TextView txtRainfall, txtHumidity, txtWindShift, txtWindSpeed;
    private String rainFall, humidity, windShift, windSpeed;
    private int windDirections;
    private double windDirectionsCal;
    private int windSpeedCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //실시간 날씨 정보
        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        txtStatus = findViewById(R.id.txtStatus);
        imgWeather = findViewById(R.id.imgWeather);

        //강수량, 습도, 풍향, 풍속
        txtRainfall = findViewById(R.id.txtRainfall);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWindShift = findViewById(R.id.txtWindShift);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);

        gpsTracker = new GpsTracker(this);
        geocoder = new Geocoder(this);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel.init(this);
        viewModel.getWeather().observe(this, vilageFcst -> {
            if (viewModel != null) {
                /*
                    SKY : 하늘상태(맑음(1), 구름 많음(3), 흐림(4)
                    PTY : 강수형태(없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
                    TMP : 1시간 기온(˚)
                    POP : 강수 확률(%)
                    REH : 습도(%)
                    VEC : 풍향(동서남북)
                    WSD : 풍속(m/s)
                 */
                weatherModel = viewModel.getWeather().getValue();

                sky_value = weatherModel.getResponse().getBody().getItems().getItem().get(5).getFcstValue();                 //Category : SKY
                pty_value = weatherModel.getResponse().getBody().getItems().getItem().get(6).getFcstValue();                 //Category : PTY
                fcstTime = Integer.parseInt(weatherModel.getResponse().getBody().getItems().getItem().get(0).getFcstTime()); //Category : TMP
                temp = weatherModel.getResponse().getBody().getItems().getItem().get(0).getFcstValue();                      //Category : TMP
                rainFall = weatherModel.getResponse().getBody().getItems().getItem().get(7).getFcstValue();                  //Category : POP
                humidity = weatherModel.getResponse().getBody().getItems().getItem().get(10).getFcstValue();                 //Category : REH
                windShift = weatherModel.getResponse().getBody().getItems().getItem().get(3).getFcstValue();                 //Category : VEC
                windSpeed = weatherModel.getResponse().getBody().getItems().getItem().get(4).getFcstValue();                 //Category : WSD

                /*
                    0 : N(북)
                    1 : NNE(북북동)
                    2 : NE(북동)
                    3 : ENE(동북동)
                    4 : E(동)
                    5 : ESE(동남동)
                    6 : SE(남동)
                    7 : SSE(남남동)
                    8 : S(남)
                    9 : SSW(남남서)
                    10 : SW(남서)
                    11 : WSW(서남서)
                    12 : W(서)
                    13 : WNW(서북서)
                    14 : NW(북서)
                    15 : NNW(북북서)
                    16 : N(북)
                 */
                //풍향값에 따른 16방위 변환
                windDirectionsCal = (Integer.parseInt(windShift) + 22.5 * 0.5) / 22.5;
                windDirections = (int)Math.floor(windDirectionsCal);
                //풍속에 따른 바람값
                windSpeedCal = (int)Math.floor(Double.parseDouble(windSpeed));
                
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
                txtTemp.setText(getString(R.string.temp, temp));

                //Weather Image & Status
                switch (pty_value) {
                    //특수 상황 없음
                    case "0":
                        //SKY 0 : 맑음
                        switch (sky_value) {
                            case "1":
                                txtStatus.setText("맑음");
                                //예보 기준이 낮인지 밤인지
                                if (1759 >= fcstTime) {
                                    imgWeather.setImageResource(R.drawable.sunny_day);
                                } else {
                                    imgWeather.setImageResource(R.drawable.sunny_night);
                                }
                                break;
                            //SKY 3 : 구름 많음
                            case "3":
                                txtStatus.setText("구름 많음");
                                //예보 기준이 낮인지 밤인지
                                if (1759 >= fcstTime) {
                                    imgWeather.setImageResource(R.drawable.cloudy_day);
                                } else {
                                    imgWeather.setImageResource(R.drawable.cloudy_night);
                                }
                                break;
                            //SKY 4 : 흐림
                            case "4":
                                txtStatus.setText("흐림");
                                imgWeather.setImageResource(R.drawable.cloudy);
                                break;
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

                //Rainfall
                txtRainfall.setText(getString(R.string.rainfall, rainFall));

                //Humidity
                txtHumidity.setText(getString(R.string.humidity, humidity));

                //WindShift
                switch (windDirections) {
                    case 0:
                    case 16:
                        txtWindShift.setText("북");
                        break;
                    case 1:
                        txtWindShift.setText("북북동");
                        break;
                    case 2:
                        txtWindShift.setText("북동");
                        break;
                    case 3:
                        txtWindShift.setText("동북동");
                        break;
                    case 4:
                        txtWindShift.setText("동");
                        break;
                    case 5:
                        txtWindShift.setText("동남동");
                        break;
                    case 6:
                        txtWindShift.setText("남동");
                        break;
                    case 7:
                        txtWindShift.setText("남남동");
                        break;
                    case 8:
                        txtWindShift.setText("남");
                        break;
                    case 9:
                        txtWindShift.setText("남남서");
                        break;
                    case 10:
                        txtWindShift.setText("남서");
                        break;
                    case 11:
                        txtWindShift.setText("서남서");
                    case 12:
                        txtWindShift.setText("서");
                        break;
                    case 13:
                        txtWindShift.setText("서북서");
                        break;
                    case 14:
                        txtWindShift.setText("북서");
                        break;
                    case 15:
                        txtWindShift.setText("북북서");
                        break;
                }

                //WindSpeed
                if (windSpeedCal < 4) {
                    txtWindSpeed.setText(getString(R.string.windspeed1, windSpeed));
                }
                else if (windSpeedCal < 9) {
                    txtWindSpeed.setText(getString(R.string.windspeed2, windSpeed));
                }
                else if (windSpeedCal < 14) {
                    txtWindSpeed.setText(getString(R.string.windspeed3, windSpeed));
                }
                else {
                    txtWindSpeed.setText(getString(R.string.windspeed4, windSpeed));
                }
//                    adapter.setItems(weatherModel.getResponse().getBody().getItems().getItem());
//                    recyclerView.setAdapter(adapter);
            }
        });
    }
}