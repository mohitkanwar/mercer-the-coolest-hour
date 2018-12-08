package com.mohitkanwar.weather.coolesthour.model;

public class Temperature implements Comparable<Temperature> {
    private double value;
    private TemperatureUnit unit;

    public Temperature() {
        this.unit = TemperatureUnit.CELCIUS;
    }

    public Temperature(TemperatureUnit unit) {
        this.unit = unit;
    }

    public Temperature(long value, TemperatureUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public TemperatureUnit getUnit() {
        return unit;
    }

    public void setUnit(TemperatureUnit unit) {
        this.unit = unit;
    }

    //TODO
    private double getValueInKelvin() {
        return value;
    }

    @Override
    public int compareTo(Temperature o) {
        if (this.unit.equals(o.unit)) {
            return Double.compare(this.value, o.value);
        } else {
            return Double.compare(this.getValueInKelvin(), o.getValueInKelvin());
        }
    }
}
