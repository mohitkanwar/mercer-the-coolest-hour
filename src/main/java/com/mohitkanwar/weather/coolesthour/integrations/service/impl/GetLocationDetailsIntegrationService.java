package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.LocationDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

interface GetLocationDetailsIntegrationService {
    @GET("postalcodes/search?apikey=8qfertiTVqZ4lpoO3hVs5vBKykuMvyNK")
    Call<List<LocationDetails>> getDetails(@Query("q") String zipCode);
}
