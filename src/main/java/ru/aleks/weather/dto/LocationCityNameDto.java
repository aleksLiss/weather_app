package ru.aleks.weather.dto;

public class LocationCityNameDto {

    private String city;

    public LocationCityNameDto() {
    }

    public LocationCityNameDto(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
