package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.model.Location;
import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;

import java.util.List;


public interface TemperatureForecastService {
    List<TemperatureAtTime> getTemperatureForeCastForGeoPosition(Location location);
}
