package com.mohitkanwar.weather.coolesthour.integrations.response.model;

public class TemperatureData {
    private long time;
    private double temperature;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
