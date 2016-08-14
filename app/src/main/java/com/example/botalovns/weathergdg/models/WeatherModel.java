package com.example.botalovns.weathergdg.models;

import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.enums.DirectionOfTheWindEnum;

public class WeatherModel {
    private CloudinessEnum cloudiness;
    private int temp;
    private int powerWind;  // Сила ветра
    private DirectionOfTheWindEnum directionOfTheWind; // Направление ветра
    private int pressure;  // Давление

    public CityModel city;

    public WeatherModel(int temp, CloudinessEnum cloudiness, int powerWind, DirectionOfTheWindEnum directionOfTheWind, int pressure){
        this.temp = temp;
        this.cloudiness = cloudiness;
        this.powerWind = powerWind;
        this.directionOfTheWind = directionOfTheWind;
        this.pressure = pressure;
    }

    public WeatherModel(){
        this.temp = -273;
        this.cloudiness = CloudinessEnum.NONE;
        this.powerWind = -1;
        this.directionOfTheWind = DirectionOfTheWindEnum.NONE;
        this.pressure = -1;
    }

    public int getTemp(){
        return temp;
    }
    public int getPowerWind()
    {
        return powerWind;
    }
    public  int getPressure(){
        return pressure;
    }
    public DirectionOfTheWindEnum getDirectionOfTheWind(){
        if(directionOfTheWind!=null) {
            return directionOfTheWind;
        }
        return DirectionOfTheWindEnum.NONE;
    }
    public CloudinessEnum getCloudiness(){
        if(cloudiness!=null) {
            return cloudiness;
        }
        return CloudinessEnum.NONE;
    }

    public void setTemp(int temp){
        this.temp = temp;
    }
    public void setCloudiness(CloudinessEnum cloudiness){
        this.cloudiness = cloudiness;
    }
    public void setPowerWind(int powerWind){
        this.powerWind = powerWind;
    }
    public void setDirectionOfTheWind(DirectionOfTheWindEnum directionOfTheWind){
        this.directionOfTheWind = directionOfTheWind;
    }
    public void setPressure(int pressure){
        this.pressure = pressure;
    }
}
