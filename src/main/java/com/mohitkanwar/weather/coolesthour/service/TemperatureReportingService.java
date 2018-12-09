package com.mohitkanwar.weather.coolesthour.service;

import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;

import java.util.List;

public interface TemperatureReportingService {
    void printTemperatures(List<TemperatureAtTime> temperatures);
}
