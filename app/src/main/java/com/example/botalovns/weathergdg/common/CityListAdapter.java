package com.example.botalovns.weathergdg.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.botalovns.weathergdg.ListCitiesActivity;
import com.example.botalovns.weathergdg.MainActivity;
import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

import java.util.ArrayList;

public class CityListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CityModel> objects;

    public CityListAdapter(Context context, ArrayList<CityModel> cities) {
        ctx = context;
        objects = cities;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_city, parent, false);
        }

        final CityModel city = getProduct(position);

        ((TextView) view.findViewById(R.id.nameCityFromDB)).setText(city.getName());
        String temp = "-";
        if(city.getWeather().getTemp()>-273)
        {
            temp = Integer.toString(city.getWeather().getTemp()) + "°C";
        }
        ((TextView) view.findViewById(R.id.tempFromDb)).setText(temp);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("city_name", city.getName());
                ctx.startActivity(intent);
            }
        });
        view.setOnCreateContextMenuListener((Activity)ctx);

        return view;
    }

    CityModel getProduct(int position) {
        return ((CityModel) getItem(position));
    }
}
