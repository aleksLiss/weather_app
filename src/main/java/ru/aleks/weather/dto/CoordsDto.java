package ru.aleks.weather.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class CoordsDto {

    @JsonSetter("lat")
    private double lat;
    @JsonSetter("lon")
    private double lon;

    public CoordsDto() {
    }

    public CoordsDto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
