package com.mohitkanwar.weather.coolesthour.service.impl;

import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.TemperatureNotFoundException;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.ResponseCodeAndBody;
import com.mohitkanwar.weather.coolesthour.integrations.service.impl.HTTPClientService;
import com.mohitkanwar.weather.coolesthour.model.Location;
import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TemperatureForecastServiceImplTest {
    private static StringBuilder successResponse = new StringBuilder("{\n" +
            "\t\"latitude\": 1,\n" +
            "\t\"longitude\": 1,\n" +
            "\t\"timezone\": \"Etc/GMT\",\n" +
            "\t\"hourly\": {\n" +
            "\t\t\"summary\": \"Rain starting this evening.\",\n" +
            "\t\t\"icon\": \"rain\",\n" +
            "\t\t\"data\": [{\n" +
            "\t\t\t\"time\": 1544335200,\n" +
            "\t\t\t\"temperature\": 27.17\n" +
            "\t\t}");
    @Autowired
    private TemperatureForecastServiceImpl temperatureForecastService;
    @MockBean
    private HTTPClientService httpClientService;

    @BeforeClass
    public static void setup() {
        for (int i = 0; i < 24; i++) {
            String hourlyTemp = ", {\n" +
                    "\t\t\t\"time\": " +
                    LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond()
                    + ",\n" +
                    "\t\t\t\"temperature\": " + i + "\n" +
                    "\t\t} \n";
            successResponse.append(hourlyTemp);
        }
        successResponse.append(
                "\t\t]\n" +
                        "\t},\n" +
                        "\t\"offset\": 0\n" +
                        "}");
    }

    @Test
    public void getTemperatureForeCastForGeoPosition() throws IOException {
        ResponseCodeAndBody responseCodeAndBody = new ResponseCodeAndBody();
        responseCodeAndBody.setResponseCode(200);
        responseCodeAndBody.setBody(successResponse.toString());
        when(httpClientService.get(anyString())).thenReturn(responseCodeAndBody);
        Location location = new Location();
        location.setLongitude(1);
        location.setLatitude(1);
        List<TemperatureAtTime> temperatures = temperatureForecastService.getTemperatureForeCastForGeoPosition(location);
        assertEquals(24, temperatures.size());
    }

    @Test(expected = TemperatureNotFoundException.class)
    public void temperaturesNotFound() throws IOException {
        ResponseCodeAndBody responseCodeAndBody = new ResponseCodeAndBody();
        responseCodeAndBody.setResponseCode(200);
        responseCodeAndBody.setBody("{\"latitude\":1,\"longitude\":1,\"hourly\":{\"data\":[" +
                "{\"time\":1,\"temperature\":1}" +
                "]}}");
        when(httpClientService.get(anyString())).thenReturn(responseCodeAndBody);
        Location location = new Location();
        location.setLongitude(1);
        location.setLatitude(1);
        temperatureForecastService.getTemperatureForeCastForGeoPosition(location);
    }

    @Test(expected = SetupException.class)
    public void testIOException() throws IOException {
        when(httpClientService.get(anyString())).thenThrow(new IOException());
        Location location = new Location();
        location.setLongitude(1);
        location.setLatitude(1);
        temperatureForecastService.getTemperatureForeCastForGeoPosition(location);
    }
}