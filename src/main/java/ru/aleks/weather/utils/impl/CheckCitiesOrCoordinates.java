package ru.aleks.weather.utils.impl;

import org.springframework.stereotype.Component;
import ru.aleks.weather.utils.CheckLocations;
import ru.aleks.weather.utils.Locations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.aleks.weather.utils.Locations.CITY;
import static ru.aleks.weather.utils.Locations.COORDINATES;

@Component
public class CheckCitiesOrCoordinates implements CheckLocations {

    private static final String REGEX_CITIES = "^[A-Za-z]+$";
    private static final String REGEX_COORDINATES = "^\\s*([+-]?\\d+(?:\\.\\d+)?)\\s*(?:,|\\s)\\s*([+-]?\\d+(?:\\.\\d+)?)\\s*$";

    @Override
    public boolean checkLocationByRegex(Enum<Locations> type, String location) {
        switch (type) {
            case CITY:
                Pattern patternCities = Pattern.compile(REGEX_CITIES);
                Matcher matcherCities = patternCities.matcher(location);
                return matcherCities.matches();
            case COORDINATES:
                Pattern patternCoord = Pattern.compile(REGEX_COORDINATES);
                Matcher matcherCoord = patternCoord.matcher(location);
                return matcherCoord.matches();
            default:
                return false;
        }
    }
            /*
    Корректные варианты (ожидается успешный парсинг):
52.2296756, 21.0122287 +
-52.2296756, -21.0122287 - !!!
52,21 +
52.2296756 , 21.0122287 + (с пробелами вокруг)
+52.2296756, +21.0122287 - !!! (знак +, зависит от реализации)
0,0 +++++++++++
-0.0, 0.0 ---
52.2296756 21.0122287 +++++++++++ (пробел без запятой)
Некорректные / проблемные варианты (ожидается отклонение или ошибка): все не проходят кроме последнего
52.229.6756, 21.0122287 --- (две десятичные точки в числе)
52.2296756; 21.0122287 -----------(точка с запятой вместо запятой)

52, (отсутствует вторая координата)
,21 (отсутствует первая координата)
abc, def (неначисловые значения)
, (пустые значения)
`` (пустая строка)
52.2296756,21.0122287,10 (слишком много чисел)
52.2296756,- (некорректный минус без числа)
+ , - (знаки без чисел)
N52.2296756, E21.0122287 (буквы направления, если не поддерживаются)
52.2296756,21.0122287\n (с символом новой строки)
    42.2, 22.22
     */
}
