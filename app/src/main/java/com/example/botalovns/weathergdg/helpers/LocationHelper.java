package com.example.botalovns.weathergdg.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.botalovns.weathergdg.common.CustomLocationListener;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.CoordinatesModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationHelper{

    // Определение города по координатам
    public static CityModel getCurrentLocation(Context context) {

        CoordinatesModel coordinates=null;
        try {
            CustomLocationListener customLocationListener = new CustomLocationListener(context);
            Location location = customLocationListener.getLocation();
            coordinates = new CoordinatesModel(location.getLatitude(), location.getLongitude());

        }
        catch (Exception exc){
            coordinates=null;
        }

        if(coordinates!=null) {

            CityModel city = null;

            try {
                String cityName = null;
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(coordinates.getLatitude(),
                            coordinates.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        cityName = addresses.get(0).getLocality();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                city = new CityModel(cityName, coordinates);
            } catch (Exception exc) {
                city = null;
            }

            return city;
        }
        return null;
    }



    //Определение местоположения
   private CoordinatesModel getLocation(Context context) {
        CoordinatesModel coordinates = new CoordinatesModel(0, 0);
        try {
            CustomLocationListener customLocationListener = new CustomLocationListener(context);
            Location location = customLocationListener.getLocation();
            coordinates = new CoordinatesModel(location.getLatitude(), location.getLongitude());

        }
        catch (Exception exc){
            Log.println(Log.ERROR, "", exc.toString());
        }

        return coordinates;
    }
}
