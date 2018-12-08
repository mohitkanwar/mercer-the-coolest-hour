package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohitkanwar.weather.coolesthour.exceptions.RemoteServiceExecutionException;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.TemperatureNotFoundException;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureData;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureResponse;
import com.mohitkanwar.weather.coolesthour.model.Temperature;
import com.mohitkanwar.weather.coolesthour.model.TemperatureUnit;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemperatureForecastServiceImpl implements TemperatureForecastService {

    @Value("${temperature.service.location}")
    private String temperatureServiceLocation;


    @Override
    public List<Temperature> getTemperatureForeCastForGeoPosition(GeoPosition geoPosition) {
        List<Temperature> temperatures = new ArrayList<>();

        try {
            URL url = new URL(temperatureServiceLocation.replace("{lat}", String.valueOf(geoPosition.getLatitude())).replace("{long}", String.valueOf(geoPosition.getLongitude())));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                StringBuilder body = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    body.append(output);
                }

                conn.disconnect();
                Gson gson = new Gson();
                Type tempResponseType = new TypeToken<TemperatureResponse>() {
                }.getType();
                TemperatureResponse temperatureResponse = gson.fromJson(body.toString(), tempResponseType);
                LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
                for (TemperatureData data : temperatureResponse.getHourly().getData()) {
                    LocalDateTime date = Instant.ofEpochMilli(data.getTime() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (date.getDayOfYear() == tomorrow.getDayOfYear()) {
                        Temperature temperature = new Temperature();
                        temperature.setValue(data.getTemperature());
                        temperature.setUnit(TemperatureUnit.CELSIUS);
                        temperatures.add(temperature);
                    }
                }
                if (temperatures.size() == 0) {
                    throw new TemperatureNotFoundException("The service was unable to fetch relevant temperatures");
                }
            } else {
                throw new RemoteServiceExecutionException("The service at " + temperatureServiceLocation + " returned with code" + conn.getResponseCode());

            }

        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + temperatureServiceLocation);
        }
        return temperatures;
    }
}
