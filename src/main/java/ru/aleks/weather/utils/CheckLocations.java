package ru.aleks.weather.utils;

public interface CheckLocations {

    boolean checkLocationByRegex(Enum<Locations> typeLocation, String location);
}
