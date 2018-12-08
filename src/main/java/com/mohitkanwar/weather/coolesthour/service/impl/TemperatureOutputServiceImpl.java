package com.mohitkanwar.weather.coolesthour.service.impl;

import com.mohitkanwar.weather.coolesthour.model.Temperature;
import com.mohitkanwar.weather.coolesthour.service.TemperatureOutputService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemperatureOutputServiceImpl implements TemperatureOutputService {
    @Override
    public void printTemperatures(List<Temperature> temperatures) {
        Temperature minimumTemperature = getMinimumTemp(temperatures);
        for (int i = 0; i < temperatures.size(); i++) {
            if (temperatures.get(i).compareTo(minimumTemperature) == 0) {
                System.out.println("---> The temperature at hour " + i + " is " + temperatures.get(i).getValue() + " " + temperatures.get(i).getUnit() + "<---");
            } else {
                System.out.println("The temperature at hour " + i + " is " + temperatures.get(i).getValue() + " " + temperatures.get(i).getUnit());
            }
        }
    }

    private Temperature getMinimumTemp(List<Temperature> temperatures) {
        Temperature minValue = temperatures.get(0);
        for (int i = 1; i < temperatures.size(); i++) {
            if (temperatures.get(i).compareTo(minValue) < 1) {
                minValue = temperatures.get(i);
            }
        }
        return minValue;
    }
}
