package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.model.Temperature;

import java.util.List;

public interface TemperatureOutputService {
    void printTemperatures(List<Temperature> temperatures);
}
