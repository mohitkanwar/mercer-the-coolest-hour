package com.mohitkanwar.weather.coolesthour.integrations.response.model;

public class LocationDetails {

    private String PrimaryPostalCode;
    private Country Country;
    private GeoPosition GeoPosition;


    public String getPrimaryPostalCode() {
        return PrimaryPostalCode;
    }

    public void setPrimaryPostalCode(String primaryPostalCode) {
        PrimaryPostalCode = primaryPostalCode;
    }

    public com.mohitkanwar.weather.coolesthour.integrations.response.model.Country getCountry() {
        return Country;
    }

    public void setCountry(com.mohitkanwar.weather.coolesthour.integrations.response.model.Country country) {
        Country = country;
    }

    public com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition getGeoPosition() {
        return GeoPosition;
    }

    public void setGeoPosition(com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition geoPosition) {
        GeoPosition = geoPosition;
    }
}
