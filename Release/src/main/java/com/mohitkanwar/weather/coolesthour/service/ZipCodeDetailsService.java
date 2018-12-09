package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.model.Location;

public interface ZipCodeDetailsService {
    Location getLocationFromZipCode(String zipcode);
}
