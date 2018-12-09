package com.mohitkanwar.weather.coolesthour.service.impl;

import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;
import com.mohitkanwar.weather.coolesthour.service.TemperatureReportingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TemperatureReportingServiceImpl implements TemperatureReportingService {
    @Value("${report.date.format}")
    private String reportDateFormat;

    @Override
    public void printTemperatures(List<TemperatureAtTime> temperatures) {
        TemperatureAtTime minimumTemperature = getMinimumTemp(temperatures);
        for (int i = 0; i < temperatures.size(); i++) {
            if (temperatures.get(i).compareTo(minimumTemperature) == 0) {
                reportDateFormat = "dd MMM yyyy - hh:mm a";
                System.out.println("--The-Coolest-Hour--> The temperature at hour " + temperatures.get(i).getTime().format(DateTimeFormatter.ofPattern(reportDateFormat)) + " is " + temperatures.get(i).getValue() + "°C<---");
            } else {
                System.out.println("The temperature at hour " + temperatures.get(i).getTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm a")) + " is " + temperatures.get(i).getValue() + "°C");
            }
        }
    }

    private TemperatureAtTime getMinimumTemp(List<TemperatureAtTime> temperatures) {
        TemperatureAtTime minValue = temperatures.get(0);
        for (int i = 1; i < temperatures.size(); i++) {
            if (temperatures.get(i).compareTo(minValue) < 1) {
                minValue = temperatures.get(i);
            }
        }
        return minValue;
    }
}
