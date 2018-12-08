package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.HourlyData;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureData;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureResponse;
import com.mohitkanwar.weather.coolesthour.model.Temperature;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest({"testargument"})
public class TemperatureForecastServiceImplTest {

    @Autowired
    private TemperatureForecastService service;
    @Autowired
    @Qualifier("temperatureService")
    private Retrofit temperatureService;
    @MockBean
    private TemperatureDetailsIntegrationService temperatureDetailsIntegrationService;
    @MockBean
    private Call<TemperatureResponse> call;
    @Test
    public void getTemperatureForeCastForGeoPosition() throws IOException {
        GeoPosition testGeoPosition = new GeoPosition();
        testGeoPosition.setLatitude(1);
        testGeoPosition.setLongitude(1);
        when(temperatureDetailsIntegrationService.getDetails("1","1")).thenReturn(call);
        TemperatureResponse temperatureResponse = new TemperatureResponse();
        HourlyData hourlyData = new HourlyData();
        TemperatureData[] data = new TemperatureData[7];
        data[0]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),20);
        data[1]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),21);
        data[2]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),22);
        data[3]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),23);
        data[4]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),24);
        data[5]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),25);
        data[6]= new TemperatureData(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond(),26);

        hourlyData.setData(data);
        temperatureResponse.setHourly(hourlyData);
        Response<TemperatureResponse> response = Response.success(temperatureResponse);
        when(call.execute()).thenReturn(response);
        List<Temperature> temperatureList = service.getTemperatureForeCastForGeoPosition(testGeoPosition);
        assertEquals(7,temperatureList.size());
    }
}