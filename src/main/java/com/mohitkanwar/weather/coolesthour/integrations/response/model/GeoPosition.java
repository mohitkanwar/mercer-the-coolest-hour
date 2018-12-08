package com.mohitkanwar.weather.coolesthour.integrations.response.model;

public class GeoPosition {

    private double Latitude;
    private double Longitude;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
