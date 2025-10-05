package ru.aleks.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LocationAnswerDto {

    @JsonProperty("main")
    private MainDto mainDto;
    @JsonProperty("name")
    private String city;
    @JsonProperty("sys")
    private CountryDto countryDto;
    @JsonProperty("weather")
    private WeatherDto[] weatherDto;

    public LocationAnswerDto() {
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

}
