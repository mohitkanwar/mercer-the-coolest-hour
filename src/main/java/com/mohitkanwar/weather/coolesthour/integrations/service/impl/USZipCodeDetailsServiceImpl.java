package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.exceptions.RemoteServiceExecutionException;
import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.USZipCodeNotFound;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.GeoPosition;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.LocationDetails;
import com.mohitkanwar.weather.coolesthour.service.ZipCodeDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

@Service
public class USZipCodeDetailsServiceImpl implements ZipCodeDetailsService {

    @Value("${accuweather.service.location}")
    private String baseUrl;

    @Override
    public GeoPosition getLocationFromZipCode(String zipcode) {

        Retrofit accuweather = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetLocationDetailsIntegrationService service = accuweather.create(GetLocationDetailsIntegrationService.class);
        Call<List<LocationDetails>> call = service.getDetails(zipcode);
        try {
            Response<List<LocationDetails>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                List<LocationDetails> locations = response.body();
                for (LocationDetails locationDetails : locations) {
                    if (locationDetails.getCountry().getID().equalsIgnoreCase("US")) {
                        return locationDetails.getGeoPosition();

                    }
                }
                throw new USZipCodeNotFound("We were unable to map the provided zipcode [" + zipcode + "] to a valid geo-location in U.S.");
            } else {
                throw new RemoteServiceExecutionException("The service at " + baseUrl + " returned with error code" + response.code() + " and message is " + response.raw());
            }

        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + baseUrl);
        }


    }
}
