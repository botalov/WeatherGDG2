package com.example.botalovns.weathergdg.models;

import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.enums.DirectionOfTheWindEnum;

/**
 * Created by BotalovNS on 05.07.2016.
 */
public class WeatherModel {
    public CloudinessEnum cloudiness;
    public int temp;
    public int powerWind;  // Сила ветра
    public DirectionOfTheWindEnum directionOfTheWind; // Направление ветра
    public int pressure;  // Давление

    public WeatherModel(int temp, CloudinessEnum cloudiness, int powerWind, DirectionOfTheWindEnum directionOfTheWind, int pressure){
        this.temp = temp;
        this.cloudiness = cloudiness;
        this.powerWind = powerWind;
        this.directionOfTheWind = directionOfTheWind;
        this.pressure = pressure;
    }
}
