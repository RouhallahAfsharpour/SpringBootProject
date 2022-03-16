package com.example.login_project.model;

public class CityWeather {

    private String cityName;
    private Double currentTemp;
    private Double humidity;
    private String condition;

    public CityWeather(String cityName, Double currentTemp, Double humidity, String condition) {
        this.cityName = cityName;
        this.currentTemp = currentTemp;
        this.humidity = humidity;
        this.condition = condition;
    }

    public String getCityName() {
        return cityName;
    }

    public Double getCurrentTemp() {
        return currentTemp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return  "{city=" + cityName +
                ", current Temp=" + currentTemp +
                ", humidity=" + humidity +
                ", condition=" + condition+'}';
    }
}
