package ru.aleks.weather.dto;

public class FoundWeatherDto {

    private String name;
    private String temperature;
    private int humidity;
    private String country;
    private String description;

    public FoundWeatherDto() {
    }

    public FoundWeatherDto(String name, String temperature, int humidity, String country, String description) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.country = country;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
