package com.mohitkanwar.weather.coolesthour.integrations.response.model;

public class LocationDetails {

    private String PrimaryPostalCode;
    private Country Country;
    private GeoPosition GeoPosition;
    private String EnglishName;

    public String getPrimaryPostalCode() {
        return PrimaryPostalCode;
    }

    public void setPrimaryPostalCode(String primaryPostalCode) {
        PrimaryPostalCode = primaryPostalCode;
    }

    public Country getCountry() {
        return Country;
    }

    public void setCountry(Country country) {
        Country = country;
    }

    public GeoPosition getGeoPosition() {
        return GeoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        GeoPosition = geoPosition;
    }

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String englishName) {
        EnglishName = englishName;
    }
}
