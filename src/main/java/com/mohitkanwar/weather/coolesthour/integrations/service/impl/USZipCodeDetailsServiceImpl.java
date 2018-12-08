package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohitkanwar.weather.coolesthour.exceptions.RemoteServiceExecutionException;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.USZipCodeNotFound;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.LocationDetails;
import com.mohitkanwar.weather.coolesthour.service.ZipCodeDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class USZipCodeDetailsServiceImpl implements ZipCodeDetailsService {

    @Value("${accuweather.service.location}")
    private String temperatureAPI;

    @Override
    public GeoPosition getLocationFromZipCode(String zipcode) {


        try {
            URL url = new URL(temperatureAPI + zipcode);
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
                Type listType = new TypeToken<List<LocationDetails>>() {
                }.getType();
                List<LocationDetails> locations = gson.fromJson(body.toString(), listType);
                for (LocationDetails locationDetails : locations) {
                    if (locationDetails.getCountry().getID().equalsIgnoreCase("US")) {
                        return locationDetails.getGeoPosition();
                    }
                }
                throw new USZipCodeNotFound("We were unable to map the provided zipcode [" + zipcode + "] to a valid geo-location in U.S.");
            } else {
                throw new RemoteServiceExecutionException("The service at " + temperatureAPI + " returned with error code" + conn.getResponseCode());
            }

        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + temperatureAPI);
        }

    }
}
