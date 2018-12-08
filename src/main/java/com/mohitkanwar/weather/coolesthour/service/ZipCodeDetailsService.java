package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;

public interface ZipCodeDetailsService {
    GeoPosition getLocationFromZipCode(String zipcode);
}
