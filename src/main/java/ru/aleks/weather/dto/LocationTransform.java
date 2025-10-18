package ru.aleks.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationTransform {

    @JsonProperty("main")
    private MainDto mainDto;
    @JsonProperty("name")
    private String city;
    @JsonProperty("sys")
    private CountryDto countryDto;
    @JsonProperty("weather")
    private WeatherDto[] weatherDto;
    @JsonProperty("coord")
    private CoordsDto coordsDto;

    public LocationTransform() {
    }

    public CoordsDto getCoordsDto() {
        return coordsDto;
    }

    public double getTemperature() {
        return mainDto.getTemp();
    }

    public int getHumidity() {
        return mainDto.getHumidity();
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return countryDto.getCountry();
    }

    public String getDescription() {
        return weatherDto[0].getDescription();
    }

    public String getWeather() {
        return weatherDto[0].getMainWeather();
    }

    public String getIcon() {
        return weatherDto[0].getIcon();
    }
}
