package com.example.botalovns.weathergdg.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.botalovns.weathergdg.ListCitiesActivity;
import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.helpers.DatabaseHelper;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

import java.util.ArrayList;

public class SearchCityListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CityModel> objects;
    DatabaseHelper databaseHelper;

    public SearchCityListAdapter(Context context, ArrayList<CityModel> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databaseHelper = new DatabaseHelper(context);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_city_search, parent, false);
        }

        final CityModel city = getCity(position);
        ((TextView) view.findViewById(R.id.nameCity)).setText(city.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!databaseHelper.existsCity(city.getName())) {

                    DefinitionWeatherAsync definitionWeatherAsync  = new DefinitionWeatherAsync(city, ctx);
                    definitionWeatherAsync.setDelegate(new DefinitionWeatherAsync.AsyncResponseWeather() {
                        @Override
                        public void processFinish(WeatherModel output) {
                            city.setWeather(output);
                            databaseHelper.addCity(city);

                            Intent intent = new Intent(ctx, ListCitiesActivity.class);
                            ctx.startActivity(intent);
                        }
                    });
                    definitionWeatherAsync.execute();

                }
            }
        });

        return view;
    }

    CityModel getCity(int position) {
        return ((CityModel) getItem(position));
    }
}