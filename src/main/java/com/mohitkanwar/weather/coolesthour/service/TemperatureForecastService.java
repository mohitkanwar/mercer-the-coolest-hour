package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.model.Temperature;

import java.util.List;


public interface TemperatureForecastService {
    List<Temperature> getTemperatureForeCastForGeoPosition(GeoPosition geoPosition);
}
