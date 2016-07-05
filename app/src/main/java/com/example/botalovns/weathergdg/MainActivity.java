package com.example.botalovns.weathergdg;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.botalovns.weathergdg.helpers.DatabaseHelper;
import com.example.botalovns.weathergdg.helpers.LocationHelper;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.CoordinatesModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Определяем местоположение по GPS
            CoordinatesModel coordinates = LocationHelper.getLocation();
            if(coordinates == null){
                throw new Exception("Не удалось определить координаты устройства");
            }
            //Определяем город по координатам
            CityModel city = LocationHelper.definitionCity(coordinates);
            if(city == null){
                throw new Exception("Не удалось определить город");
            }

            List<CityModel> citiesFromDB = DatabaseHelper.getCities();
            // Если найденный город отсутствует в БД, то добавляем его
            if(!citiesFromDB.contains(city)){
                DatabaseHelper.addCity(city);
            }


        }
        catch (Exception exc){
            Log.println(Log.ERROR, "", exc.toString());
        }

    }
}
