package ru.aleks.weather.dto;

public class LocationGetDto {

    private String location;

    public LocationGetDto() {
    }

    public LocationGetDto(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
