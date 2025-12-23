package com.weather.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDto {

    @JsonProperty("name")
    private String location;
    @JsonProperty("main")
    private MainData mainData;
    @JsonProperty("weather")
    private WeatherData[] weatherData;
    @JsonProperty("coord")
    private CoordsDto coordsDto;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherResponseDto.class);

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        if (null == this.mainData) {
            return 0;
        }
        double rawTemp = this.mainData.getTemperature();
        double celsius = rawTemp - 273.15;
        return (int) Math.round(celsius);
    }

    public int getHumidity() {
        if (null == this.mainData) {
            return 0;
        }
        return this.mainData.getHumidity();
    }

    public String getDescription() {
        if (this.weatherData == null || this.weatherData.length == 0) {
            return "description is empty";
        }
        return weatherData[0].getDescription();
    }

    public String getImage() {
        if (this.weatherData == null || this.weatherData.length == 0) {
            return "image is empty";
        }
        String extension = ".png";
        String url = "https://openweathermap.org/img/wn/";
        return String.format(url + this.weatherData[0].getIcon() + extension);
    }

    public double getLatitude() {
        return coordsDto.getLatitude();
    }

    public double getLongitude() {
        return coordsDto.getLongitude();
    }

    public void setMainData(MainData mainData) {
        this.mainData = mainData;
    }

    public void setWeatherData(WeatherData[] weatherData) {
        this.weatherData = weatherData;
    }

    public void setCoordsDto(CoordsDto coordsDto) {
        this.coordsDto = coordsDto;
    }
}
