package ru.aleks.weather.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class WeatherDto {

    @JsonSetter("main")
    private String mainWeather;
    @JsonSetter("description")
    private String description;
    @JsonSetter("icon")
    private String icon;

    public WeatherDto() {
    }

    public WeatherDto(String mainWeather, String description, String icon) {
        this.mainWeather = mainWeather;
        this.description = description;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
