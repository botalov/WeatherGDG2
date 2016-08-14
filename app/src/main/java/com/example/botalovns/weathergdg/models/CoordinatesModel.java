package com.example.botalovns.weathergdg.models;

public class CoordinatesModel {
    private double latitude; // широта
    private double longitude; // долгота

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }

    public CoordinatesModel(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public CoordinatesModel(){
        this.latitude = 0;
        this.longitude = 0;
    }
}
