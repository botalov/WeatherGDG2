package com.example.botalovns.weathergdg.helpers;

import android.content.Context;

import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.common.DefinitionWeatherAsync;
import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.enums.DirectionOfTheWindEnum;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.CoordinatesModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class WeatherHelper {

    public static WeatherModel getWeather(CityModel city, Context context) {
        WeatherModel resultWeather = new WeatherModel();
        // получаем данные с внешнего ресурса
        try {
            URL url = new URL(createAddress(city.getCoordinates(), context));

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String resultJson = buffer.toString();
            int temp = -273;
            int pressure = 0;
            int powerWind = 0;
            DirectionOfTheWindEnum windDirection = DirectionOfTheWindEnum.NONE;
            CloudinessEnum cloudinessEnum = CloudinessEnum.NONE;
            try {
                JSONObject jsonObject = new JSONObject(resultJson);
                try {
                    JSONObject mainJsonObject = jsonObject.getJSONObject("main");
                    temp = (int) mainJsonObject.getDouble("temp");
                    pressure = (int) mainJsonObject.getDouble("pressure");
                    pressure = ConvertHelper.hPaTOmmHg(pressure);
                } catch (JSONException je) {
                    je.printStackTrace();
                }

                try{
                    JSONObject weatherJsonObject = jsonObject.getJSONArray("weather").getJSONObject(0);
                    int cloudiness = weatherJsonObject.getInt("id");
                    cloudinessEnum = ConvertHelper.getCloudinessEnum(cloudiness);

                }
                catch (JSONException je){
                    je.printStackTrace();
                }

                try {
                    JSONObject windObject = jsonObject.getJSONObject("wind");
                    powerWind = (int) windObject.getDouble("speed");
                    windDirection = ConvertHelper.degTOdirectionWind(windObject.getDouble("deg"));
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            resultWeather.setTemp(temp);
            resultWeather.setPressure(pressure);
            resultWeather.setPowerWind(powerWind);
            resultWeather.setDirectionOfTheWind(windDirection);
            resultWeather.setCloudiness(cloudinessEnum);
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }

        return resultWeather;
    }

    private static String createAddress(CoordinatesModel coordinates, Context context) {
        String result = "";
        result += context.getString(R.string.weather_address);
        result += ("lat=" + coordinates.getLatitude());
        result += ("&lon=" + coordinates.getLongitude());
        result += ("&appid=" + context.getString(R.string.weather_key));
        result += "&units=metric";

        return result;
    }


    public static void updateWeatherInAllCities(Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<CityModel> cities = databaseHelper.getCities();
        for (final CityModel city : cities) {
            final DefinitionWeatherAsync definitionWeatherAsync = new DefinitionWeatherAsync(city, context);
            definitionWeatherAsync.setDelegate(new DefinitionWeatherAsync.AsyncResponseWeather() {
                @Override
                public void processFinish(WeatherModel output) {
                    city.setWeather(output);
                    databaseHelper.updateCity(city);
                    //listView.invalidateViews();
                }
            });
            definitionWeatherAsync.execute();
        }
    }

    public static void updateWeather(Context context, final CityModel city) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<CityModel> cities = databaseHelper.getCities();
        final DefinitionWeatherAsync definitionWeatherAsync = new DefinitionWeatherAsync(city, context);
        definitionWeatherAsync.setDelegate(new DefinitionWeatherAsync.AsyncResponseWeather() {
            @Override
            public void processFinish(WeatherModel output) {
                city.setWeather(output);
                databaseHelper.updateCity(city);
            }
        });
        definitionWeatherAsync.execute();
    }
}
