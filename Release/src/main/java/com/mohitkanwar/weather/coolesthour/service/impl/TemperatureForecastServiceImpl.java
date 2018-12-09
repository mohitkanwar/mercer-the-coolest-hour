package com.mohitkanwar.weather.coolesthour.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.TemperatureNotFoundException;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.ResponseCodeAndBody;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureData;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureResponse;
import com.mohitkanwar.weather.coolesthour.integrations.service.impl.HTTPClientService;
import com.mohitkanwar.weather.coolesthour.model.Location;
import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemperatureForecastServiceImpl implements TemperatureForecastService {

    private final HTTPClientService httpClientService;
    @Value("${temperature.service.location}")
    private String temperatureServiceLocation;

    @Autowired
    public TemperatureForecastServiceImpl(HTTPClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    @Override
    public List<TemperatureAtTime> getTemperatureForeCastForGeoPosition(Location location) {
        List<TemperatureAtTime> temperatures = new ArrayList<>();

        try {
            ResponseCodeAndBody responseCodeAndBody = httpClientService.get(temperatureServiceLocation.replace("{lat}", String.valueOf(location.getLatitude())).replace("{long}", String.valueOf(location.getLongitude())));

            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            Gson gson = new Gson();
            Type type = new TypeToken<TemperatureResponse>() {
            }.getType();
            TemperatureResponse temperatureResponse = gson.fromJson(responseCodeAndBody.getBody(), type);

            for (TemperatureData data : temperatureResponse.getHourly().getData()) {
                LocalDateTime date = Instant.ofEpochMilli(data.getTime() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (date.getDayOfYear() == tomorrow.getDayOfYear()) {
                    temperatures.add(new TemperatureAtTime(data.getTemperature(), date));
                }
            }
            if (temperatures.size() == 0) {
                throw new TemperatureNotFoundException("The service was unable to fetch relevant temperatures");
            }


        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + temperatureServiceLocation);
        }
        return temperatures;
    }
}
