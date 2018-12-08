package com.mohitkanwar.weather.coolesthour;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.model.Temperature;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import com.mohitkanwar.weather.coolesthour.service.TemperatureOutputService;
import com.mohitkanwar.weather.coolesthour.service.ZipCodeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class CoolestHourApplication implements CommandLineRunner {

    private final TemperatureForecastService temperatureForecastService;
    private final TemperatureOutputService temperatureOutputService;
    private final ZipCodeDetailsService zipCodeDetailsService;
    @Value("${default.zipcode}")
    private String defaultZipcode;

    @Autowired
    public CoolestHourApplication(TemperatureForecastService temperatureForecastService, TemperatureOutputService temperatureOutputService, ZipCodeDetailsService zipCodeDetailsService) {
        this.temperatureForecastService = temperatureForecastService;
        this.temperatureOutputService = temperatureOutputService;
        this.zipCodeDetailsService = zipCodeDetailsService;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CoolestHourApplication.class);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        String zipcode;
        if (args.length < 1) {
            zipcode = defaultZipcode;
        } else {

            zipcode = args[0];
        }
        LocalDate tomorrow = LocalDate.from(LocalDate.now()).plusDays(1);
        try {
            GeoPosition geoPosition = zipCodeDetailsService.getLocationFromZipCode(zipcode);
            System.out.println("Checking the coolest weather for " + tomorrow.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " at " + zipcode);
            List<Temperature> temperatures = temperatureForecastService.getTemperatureForeCastForGeoPosition(geoPosition);
            temperatureOutputService.printTemperatures(temperatures);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


    }
}
