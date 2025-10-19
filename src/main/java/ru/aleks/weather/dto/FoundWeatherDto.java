package ru.aleks.weather.dto;

public class FoundWeatherDto {

    private String cityName;

    public FoundWeatherDto() {
    }

    public FoundWeatherDto(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
