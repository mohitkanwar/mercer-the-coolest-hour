package com.mohitkanwar.weather.coolesthour;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationBeans {
    @Value("${temperature.service.location}")
    private String darkSkyServiceLocation;

}
