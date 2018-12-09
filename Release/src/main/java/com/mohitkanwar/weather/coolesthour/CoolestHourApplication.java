package com.mohitkanwar.weather.coolesthour;

import com.mohitkanwar.weather.coolesthour.model.Location;
import com.mohitkanwar.weather.coolesthour.model.TemperatureAtTime;
import com.mohitkanwar.weather.coolesthour.service.TemperatureForecastService;
import com.mohitkanwar.weather.coolesthour.service.TemperatureReportingService;
import com.mohitkanwar.weather.coolesthour.service.ZipCodeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This application returns tomorrow's(according to system's date) hourly temperatures,
 * with coolest temperature highlighted.
 * <p>
 * It accepts a valid U.S zip code as an input. If not provided,
 * it reads the default zip code from application.properties
 */
@SpringBootApplication
public class CoolestHourApplication implements CommandLineRunner {

    private final TemperatureForecastService temperatureForecastService;
    private final TemperatureReportingService temperatureReportingService;
    private final ZipCodeDetailsService zipCodeDetailsService;
    @Value("${default.zipcode}")
    private String defaultZipcode;

    @Autowired
    public CoolestHourApplication(TemperatureForecastService temperatureForecastService, TemperatureReportingService temperatureReportingService, ZipCodeDetailsService zipCodeDetailsService) {
        this.temperatureForecastService = temperatureForecastService;
        this.temperatureReportingService = temperatureReportingService;
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
            Location location = zipCodeDetailsService.getLocationFromZipCode(zipcode);
            System.out.println("Checking the weather for " + tomorrow.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " at " + zipcode + "(" + location.getName() + ")");
            List<TemperatureAtTime> temperatures = temperatureForecastService.getTemperatureForeCastForGeoPosition(location);
            temperatureReportingService.printTemperatures(temperatures);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


    }
}
