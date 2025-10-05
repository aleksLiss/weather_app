package ru.aleks.weather.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class WeatherDto {

    @JsonSetter("main")
    private String mainWeather;
    private String description;

    public WeatherDto() {
    }

    public WeatherDto(String mainWeather, String description) {
        this.mainWeather = mainWeather;
        this.description = description;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public void setMainWeather(String mainWeather) {
        this.mainWeather = mainWeather;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
