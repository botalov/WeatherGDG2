package com.example.botalovns.weathergdg.models;

public class CityModel {
    private String name;
    private CoordinatesModel coordinates;
    private WeatherModel weather;

    public String getName(){
        return name;
    }
    public WeatherModel getWeather() {
        if(weather!=null) {
            return weather;
        }
        else{
            return new WeatherModel();
        }
    }
    public CoordinatesModel getCoordinates(){
        if(coordinates!=null) {
            return coordinates;
        }
        else{
            return new CoordinatesModel();
        }
    }
    public void setName(String name){
        this.name=name;
    }
    public void setCoordinates(CoordinatesModel coordinates){
        this.coordinates = coordinates;
    }
    public void setWeather(WeatherModel weather){
        this.weather = weather;
    }
    public void setCoordinates(double lat, double lon){
        this.coordinates = new CoordinatesModel(lat, lon);
    }

    public CityModel(String name, CoordinatesModel coordinates){
        this.name = name;
        this.coordinates = coordinates;
    }
    public CityModel(String name, CoordinatesModel coordinates, WeatherModel weather){
        this.name = name;
        this.coordinates = coordinates;
        this.weather = weather;
    }
    public CityModel(){
    }
}
