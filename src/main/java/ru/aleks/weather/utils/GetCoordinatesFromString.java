package ru.aleks.weather.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GetCoordinatesFromString {

    public double [] getCoordinates(String coordinates) {
        Pattern patt = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*[ ,]\\s*(\\d+(?:\\.\\d+)?)\\s*$");
        Matcher matcher = patt.matcher(coordinates);
        double[] arr = new double[2];
        double latitude = Double.parseDouble(matcher.group(1));
        double longitude = Double.parseDouble(matcher.group(2));
        arr[0] = latitude;
        arr[1] = longitude;
        return arr;
    }
}
