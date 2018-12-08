package com.mohitkanwar.weather.coolesthour;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class ApplicationBeans {
    @Value("${temperature.service.location}")
    private String darkSkyServiceLocation;
    @Bean
    @Qualifier("temperatureService")
    public Retrofit getDarkSkyBean(){
        Retrofit darkSky = new Retrofit.Builder()
                .baseUrl(darkSkyServiceLocation)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return darkSky;
    }
}
