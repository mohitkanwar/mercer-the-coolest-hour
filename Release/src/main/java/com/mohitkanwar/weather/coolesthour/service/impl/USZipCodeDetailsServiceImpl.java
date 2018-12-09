package com.mohitkanwar.weather.coolesthour.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.USZipCodeNotFound;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.LocationDetails;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.ResponseCodeAndBody;
import com.mohitkanwar.weather.coolesthour.integrations.service.impl.HTTPClientService;
import com.mohitkanwar.weather.coolesthour.model.Location;
import com.mohitkanwar.weather.coolesthour.service.ZipCodeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class USZipCodeDetailsServiceImpl implements ZipCodeDetailsService {

    private final HTTPClientService httpClientService;
    @Value("${location.service}")
    private String zipCodeAPI;

    @Autowired
    public USZipCodeDetailsServiceImpl(HTTPClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    @Override
    public Location getLocationFromZipCode(String zipcode) {
        try {
            ResponseCodeAndBody responseCodeAndBody = httpClientService.get(zipCodeAPI + zipcode);

            Gson gson = new Gson();
            Type type = new TypeToken<List<LocationDetails>>() {
            }.getType();
            List<LocationDetails> locationDetailsList = gson.fromJson(responseCodeAndBody.getBody(), type);

            for (LocationDetails locationDetails : locationDetailsList) {
                if (locationDetails.getCountry().getID().equalsIgnoreCase("US")) {
                    Location location = new Location();
                    location.setName(locationDetails.getEnglishName());
                    location.setLatitude(locationDetails.getGeoPosition().getLatitude());
                    location.setLongitude(locationDetails.getGeoPosition().getLongitude());
                    return location;
                }
            }
            throw new USZipCodeNotFound("We were unable to map the provided zipcode [" + zipcode + "] to a valid geo-location in U.S.");

        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + zipCodeAPI);
        }

    }
}
