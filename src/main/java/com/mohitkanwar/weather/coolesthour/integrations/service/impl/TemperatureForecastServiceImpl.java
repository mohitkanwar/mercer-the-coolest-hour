package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.exceptions.RemoteServiceExecutionException;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.TempraturesNotFoundException;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureData;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureResponse;
import com.mohitkanwar.weather.coolesthour.model.Temperature;
import com.mohitkanwar.weather.coolesthour.model.TemperatureUnit;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemperatureForecastServiceImpl implements TemperatureForecastService {

    private final Retrofit temperatureService;

    @Value("${temperature.service.location}")
    private String temperatureServiceLocation;

    @Autowired
    public TemperatureForecastServiceImpl(@Qualifier("temperatureService") Retrofit temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Override
    public List<Temperature> getTemperatureForeCastForGeoPosition(GeoPosition geoPosition) {
        List<Temperature> temperatures = new ArrayList<>();

        TemperatureDetailsIntegrationService temperatureDetailsIntegrationService = temperatureService.create(TemperatureDetailsIntegrationService.class);
        Call<TemperatureResponse> call = temperatureDetailsIntegrationService.getDetails(String.valueOf(geoPosition.getLatitude()), String.valueOf(geoPosition.getLongitude()));
        try {
            Response<TemperatureResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
                for (TemperatureData data : response.body().getHourly().getData()) {
                    LocalDateTime date = Instant.ofEpochMilli(data.getTime() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (date.getDayOfYear() == tomorrow.getDayOfYear()) {
                        Temperature temperature = new Temperature();
                        temperature.setValue(data.getTemperature());
                        temperature.setUnit(TemperatureUnit.CELCIUS);
                        temperatures.add(temperature);
                    }
                }
                if (temperatures.size() == 0) {
                    throw new TempraturesNotFoundException("The service was unable to fetch relevent temperatures");
                }
            } else {
                throw new RemoteServiceExecutionException("The service at " + temperatureServiceLocation + " returned with code" + response.code() + " and body is " + response.raw());

            }

        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + temperatureServiceLocation);
        }
        return temperatures;
    }
}
