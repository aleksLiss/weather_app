package ru.aleks.weather.dto;

public class LocationSendDto {
    private String temperature;
    private String cityName;
    private String countryName;
    private String descriptionWeather;
    private int humidity;

    public LocationSendDto() {
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDescriptionWeather() {
        return descriptionWeather;
    }

    public void setDescriptionWeather(String descriptionWeather) {
        this.descriptionWeather = descriptionWeather;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
