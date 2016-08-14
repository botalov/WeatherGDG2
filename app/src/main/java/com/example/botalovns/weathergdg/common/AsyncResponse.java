package com.example.botalovns.weathergdg.common;

import com.example.botalovns.weathergdg.models.CityModel;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<CityModel> output);
}
