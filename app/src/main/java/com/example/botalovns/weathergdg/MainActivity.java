package com.example.botalovns.weathergdg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botalovns.weathergdg.common.DefinitionLocationAsync;
import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.helpers.ConvertHelper;
import com.example.botalovns.weathergdg.helpers.DatabaseHelper;
import com.example.botalovns.weathergdg.helpers.WeatherHelper;
import com.example.botalovns.weathergdg.helpers.WebHelper;
import com.example.botalovns.weathergdg.models.CityModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCALE = 1;
    private static String[] PERMISSIONS_LOCALE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    boolean changeOrientation = false;
    String cityName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeOrientation = false;

        try {
            cityName = getIntent().getStringExtra("city_name");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCALE,
                    REQUEST_LOCALE
            );
        }

        if(!changeOrientation) {
            try {
                if (cityName == null || cityName.equals("")) {
                    if (WebHelper.isNetworkAvailable(this)) {
                        DefinitionLocationAsync definitionLocationAsync = new DefinitionLocationAsync(this);
                        definitionLocationAsync.setDelegate(new DefinitionLocationAsync.AsyncResponseLocation() {
                            @Override
                            public void processFinish(CityModel output) {
                                setDataToInterface(output);
                                WeatherHelper.updateWeatherInAllCities(getApplicationContext());
                            }
                        });
                        definitionLocationAsync.execute();
                    } else {
                        ArrayList<CityModel> cities = DatabaseHelper.getInstance(this).getCities();
                        if (cities != null && cities.size() > 0) {
                            CityModel cityModel = cities.get(0);
                            if (cityModel != null) {
                                setDataToInterface(cityModel);
                            }
                            Toast.makeText(
                                    this,
                                    getString(R.string.not_internet),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        else {
                            Toast.makeText(
                                    this,
                                    getString(R.string.not_cities_and_connection),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }
                else{
                    ArrayList<CityModel> cities = DatabaseHelper.getInstance(this).getCities();
                    CityModel cityModel = null;
                    if(cities!=null && cities.size()>0){
                        for(CityModel city : cities){
                            if(city.getName().equals(cityName)){
                                cityModel = city;
                            }
                        }
                    }

                    if(cityModel!=null) {
                        setDataToInterface(cityModel);
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        changeOrientation = true;
    }

    // Переход на активити с выбором города
    public void onChangeCityButton(View view){
        Intent intent = new Intent(this, ListCitiesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.empty_anim);
    }


    private void setDataToInterface(CityModel output){
        TextView cityText = (TextView)findViewById(R.id.cityTextView);
        cityText.setText(output.getName());

        TextView tempText = (TextView)findViewById(R.id.tempTextView);
        String temp = "-";
        try{
            temp = Integer.toString(output.getWeather().getTemp());
            tempText.setText(temp);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        TextView pressureText = (TextView)findViewById(R.id.pressureTextView);
        String pressure = "-";
        try{
            pressure = Integer.toString(output.getWeather().getPressure());
            pressureText.setText(pressure);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        TextView powerWindText = (TextView)findViewById(R.id.windPowerTextView);
        String powerWind = "-";
        try{
            powerWind = Integer.toString(output.getWeather().getPowerWind());
            powerWindText.setText(powerWind);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        TextView directionWindText = (TextView)findViewById(R.id.windTextView);
        String directionWind = "-";
        try{
            directionWind = ConvertHelper.getNameOfDirectionWind(output.getWeather().getDirectionOfTheWind(), getApplicationContext());
            directionWindText.setText(directionWind);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        ImageView imageView = (ImageView)findViewById(R.id.sunImageView);
        try{
            CloudinessEnum cloudinessEnum = output.getWeather().getCloudiness();
            if(cloudinessEnum == CloudinessEnum.SUN){
                imageView.setImageResource(R.drawable.sun);
            }
            else if(cloudinessEnum == CloudinessEnum.CLODY){
                imageView.setImageResource(R.drawable.clouds);
            }
            else if(cloudinessEnum == CloudinessEnum.SUN_CLOUDY){
                imageView.setImageResource(R.drawable.sunclouds);
            }
            else if(cloudinessEnum == CloudinessEnum.RAIN){
                imageView.setImageResource(R.drawable.cloudsrain);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.double_back_press), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
