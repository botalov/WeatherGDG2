package com.example.botalovns.weathergdg.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.enums.DirectionOfTheWindEnum;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.CoordinatesModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;
    private Context context;
    int versionDB = 2;

    public DatabaseHelper(Context context) {
        super(context, context.getString(R.string.db_name), null, Integer.parseInt(context.getString(R.string.db_version)));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            String WEATHER_TABLE_CREATE_SCRIPT = "create table "
                    + context.getString(R.string.table_weather_name) + " ("
                    + "ID integer primary key autoincrement, "
                    + "CITY_NAME text not null, "
                    + "LATITUDE real, "
                    + "LONGITUDE real,"
                    + "TEMP integer, "
                    + "CLOUDINESS text,"
                    + "POWER_WIND int,"
                    + "DIRECTION_WIND text,"
                    + "PRESSURE integer" +
                    ");";
            sqLiteDatabase.execSQL(WEATHER_TABLE_CREATE_SCRIPT);

            setDefaultCities(sqLiteDatabase);
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public void setDefaultCities(SQLiteDatabase db) {
        ArrayList<CityModel> newCities = new ArrayList<>();
        newCities.add(new CityModel("Казань", new CoordinatesModel(55.47, 49.06)));
        newCities.add(new CityModel("Пермь", new CoordinatesModel(58.00, 56.19)));
        newCities.add(new CityModel("Москва", new CoordinatesModel(55.45, 37.37)));
        newCities.add(new CityModel("Сидней", new CoordinatesModel(-33.86, 151.20)));
        newCities.add(new CityModel("Верещагино", new CoordinatesModel(58.04, 54.39)));

        for(CityModel city : newCities) {
            try {
                ContentValues cv = new ContentValues();
                cv.put("CITY_NAME", city.getName());
                cv.put("TEMP", city.getWeather().getTemp());
                cv.put("CLOUDINESS", city.getWeather().getCloudiness().toString());
                cv.put("POWER_WIND", city.getWeather().getPowerWind());
                cv.put("DIRECTION_WIND", city.getWeather().getDirectionOfTheWind().toString());
                cv.put("PRESSURE", city.getWeather().getPressure());
                cv.put("LONGITUDE", city.getCoordinates().getLongitude());// double lng = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
                cv.put("LATITUDE", city.getCoordinates().getLatitude());    //double lan = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));

                db.insert(context.getString(R.string.table_weather_name), null, cv);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + context.getString(R.string.table_weather_name));

            onCreate(sqLiteDatabase);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static DatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    // Получение всех городов и погоды в них из БД
    public ArrayList<CityModel> getCities(){
        ArrayList<CityModel> cities = new ArrayList<>();


        // Получаем информацуию о погоде для найденных городов и формируем коллекцию, которую метод и возвращает
        try{
            Cursor cursor = getWritableDatabase().query(context.getString(R.string.table_weather_name),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String cityName = cursor.getString(cursor.getColumnIndex("CITY_NAME"));
                int temp = cursor.getInt(cursor.getColumnIndex("TEMP"));
                String cloudiness = cursor.getString(cursor.getColumnIndex("CLOUDINESS"));
                int powerWind  = cursor.getInt(cursor.getColumnIndex("POWER_WIND"));
                String directionWind = cursor.getString(cursor.getColumnIndex("DIRECTION_WIND"));
                int pressure = cursor.getInt(cursor.getColumnIndex("PRESSURE"));
                double lng = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
                double lan = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));

                CloudinessEnum cloudinessEnum = CloudinessEnum.valueOf(cloudiness);
                DirectionOfTheWindEnum directionOfTheWindEnum = DirectionOfTheWindEnum.valueOf(directionWind);

                WeatherModel weatherModel = new WeatherModel(temp, cloudinessEnum, powerWind, directionOfTheWindEnum, pressure);

                CoordinatesModel coordinatesModel = new CoordinatesModel(lan, lng);
                CityModel city = new CityModel(cityName, coordinatesModel, weatherModel);
                cities.add(city);
            }
            cursor.close();
        }
        catch (Exception exc){
            exc.printStackTrace();
        }

        return cities;
    }

    // Обновление городов в БД
    public void updateCities(List<CityModel> cities){

    }

    // Обновление города в БД
    public boolean updateCity(CityModel city){
        try {
            ContentValues cv = new ContentValues();
            cv.put("CITY_NAME", city.getName());
            cv.put("TEMP", city.getWeather().getTemp());
            cv.put("CLOUDINESS", city.getWeather().getCloudiness().toString());
            cv.put("POWER_WIND", city.getWeather().getPowerWind());
            cv.put("DIRECTION_WIND", city.getWeather().getDirectionOfTheWind().toString());
            cv.put("PRESSURE", city.getWeather().getPressure());
            cv.put("LONGITUDE", city.getCoordinates().getLongitude());// double lng = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
            cv.put("LATITUDE", city.getCoordinates().getLatitude());    //double lan = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));

            String selection = "CITY_NAME = ?";
            String[] selectionArgs = new String[]{city.getName()};
            getWritableDatabase().update(context.getString(R.string.table_weather_name), cv, selection, selectionArgs);
        }
        catch (Exception exc){
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    // Добавление нового города в БД
    public boolean addCity(CityModel city){
        //
        try {
            ContentValues cv = new ContentValues();
            cv.put("CITY_NAME", city.getName());
            cv.put("TEMP", city.getWeather().getTemp());
            cv.put("CLOUDINESS", city.getWeather().getCloudiness().toString());
            cv.put("POWER_WIND", city.getWeather().getPowerWind());
            cv.put("DIRECTION_WIND", city.getWeather().getDirectionOfTheWind().toString());
            cv.put("PRESSURE", city.getWeather().getPressure());
            cv.put("LONGITUDE", city.getCoordinates().getLongitude());// double lng = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
            cv.put("LATITUDE", city.getCoordinates().getLatitude());    //double lan = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));

            getWritableDatabase().insert(context.getString(R.string.table_weather_name), null, cv);
        }
        catch (Exception exc){
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    // Удаление города из БД
    public void deleteCity(CityModel city){
        try {
            String selection = "CITY_NAME = ?";
            String[] selectionArgs = new String[]{city.getName()};
            getWritableDatabase().delete(context.getString(R.string.table_weather_name), selection, selectionArgs);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean existsCity(String name){
        boolean result = false;
        try{
            String selection = "CITY_NAME = ?";
            String[] selectionArgs = new String[]{name};

            Cursor cursor = getWritableDatabase().query(context.getString(R.string.table_weather_name),
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){
                result = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}