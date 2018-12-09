package com.mohitkanwar.weather.coolesthour.model;

import java.time.LocalDateTime;

public class TemperatureAtTime implements Comparable<TemperatureAtTime> {
    private double value;
    private LocalDateTime time;

    public TemperatureAtTime(double value, LocalDateTime time) {
        this.value = value;
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public int compareTo(TemperatureAtTime o) {
        return Double.compare(getValue(), o.getValue());
    }
}
