package com.mohitkanwar.weather.coolesthour.integrations.response.model;

public class HourlyData {
    private TemperatureData[] data;

    public TemperatureData[] getData() {
        return data;
    }

    public void setData(TemperatureData[] data) {
        this.data = data;
    }
}
