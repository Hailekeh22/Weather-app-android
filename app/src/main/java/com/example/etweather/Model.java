package com.example.etweather;

public class Model {
    private String cityName;
    private String condition;
    private double temperature;

    public Model(String cityName, String condition, double temperature) {
        this.cityName = cityName;
        this.condition = condition;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCondition() {
        return condition;
    }

    public double getTemperature() {
        return temperature;
    }
}
