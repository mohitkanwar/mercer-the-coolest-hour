package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.TemperatureResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface TemperatureDetailsIntegrationService {
    @GET("/forecast/10697ca64f318ae780cb843db9d9f651/{lat},{long}?exclude=currently,minutely,daily,alerts,flags&units=si")
    Call<TemperatureResponse> getDetails(@Path("lat") String latitude, @Path("long") String longitude);
}
