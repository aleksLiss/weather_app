package com.weather.app.service;

import com.weather.app.config.WeatherApiConfig;
import com.weather.app.dto.*;
import com.weather.app.mapper.FoundLocationDtoMapper;
import com.weather.app.mapper.WeatherDtoMapper;
import com.weather.app.model.Location;
import com.weather.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherApiServiceTest {

    @Mock
    private WeatherDtoMapper weatherDtoMapper;
    @Mock
    private LocationService locationService;
    @Mock
    private FoundLocationDtoMapper foundLocationDtoMapper;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse response;
    @Mock
    private WeatherApiConfig weatherApiConfig;
    @InjectMocks
    private WeatherApiService weatherApiService;
    private String url;

    @Test
    void whenGetListCitiesByNameThenReturnResponseBodyWithCities() throws IOException, InterruptedException {
        String city = "gomel";
        String expectedJson = "[\n" +
                "    {\n" +
                "        \"name\": \"Homyel\",\n" +
                "        \"local_names\": {\n" +
                "            \"ascii\": \"Homieĺ\",\n" +
                "            \"fr\": \"Homiel\",\n" +
                "            \"et\": \"Gomel\",\n" +
                "            \"vo\": \"Homyel\",\n" +
                "            \"nl\": \"Homel\",\n" +
                "            \"es\": \"Gómel\",\n" +
                "            \"lt\": \"Gomelis\",\n" +
                "            \"uk\": \"Гомєль\",\n" +
                "            \"fa\": \"گومل\",\n" +
                "            \"sr\": \"Гомељ\",\n" +
                "            \"feature_name\": \"Homieĺ\",\n" +
                "            \"ja\": \"ホメリ\",\n" +
                "            \"hu\": \"Homel\",\n" +
                "            \"ta\": \"கோமெல்\",\n" +
                "            \"pl\": \"Homel\",\n" +
                "            \"ka\": \"გომელი\",\n" +
                "            \"lv\": \"Homjeļa\",\n" +
                "            \"tw\": \"戈梅利\",\n" +
                "            \"no\": \"Homjel\",\n" +
                "            \"ar\": \"غوميل\",\n" +
                "            \"en\": \"Homyel\",\n" +
                "            \"bg\": \"Гомел\",\n" +
                "            \"zh\": \"戈梅利\",\n" +
                "            \"ko\": \"호몔\",\n" +
                "            \"sk\": \"Homeľ\",\n" +
                "            \"be\": \"Гомель\",\n" +
                "            \"tr\": \"Gomel\",\n" +
                "            \"ru\": \"Гомель\",\n" +
                "            \"sg\": \"戈梅利\",\n" +
                "            \"he\": \"הומל\",\n" +
                "            \"el\": \"Γόμελ\",\n" +
                "            \"eo\": \"Homel\",\n" +
                "            \"th\": \"กอเมล\",\n" +
                "            \"ur\": \"گومل\",\n" +
                "            \"de\": \"Homel\"\n" +
                "        },\n" +
                "        \"lat\": 52.4238936,\n" +
                "        \"lon\": 31.0131698,\n" +
                "        \"country\": \"BY\",\n" +
                "        \"state\": \"Homyel Region\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"Гомель\",\n" +
                "        \"local_names\": {\n" +
                "            \"ascii\": \"Homieĺ\",\n" +
                "            \"feature_name\": \"Homieĺ\",\n" +
                "            \"be\": \"Гомель\",\n" +
                "            \"lt\": \"Gomelis\",\n" +
                "            \"pl\": \"Homel\",\n" +
                "            \"ru\": \"Гомель\"\n" +
                "        },\n" +
                "        \"lat\": 55.2999833,\n" +
                "        \"lon\": 28.7762135,\n" +
                "        \"country\": \"BY\",\n" +
                "        \"state\": \"Vitsebsk Region\"\n" +
                "    }\n" +
                "]";
        when(weatherApiConfig.getToken()).thenReturn("fake_token");
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(expectedJson);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        String result = weatherApiService.getListCitiesByName(city);
        assertThat(expectedJson).isEqualTo(result);
        verify(httpClient).send(argThat(request -> request.uri().toString().contains("gomel")), any());
    }

    @Test
    void whenGetListCitiesByNameThenThrowRuntimeException() throws IOException, InterruptedException {
        String city = "london";
        when(weatherApiConfig.getToken()).thenReturn("invalid_token");
        when(response.statusCode()).thenReturn(401);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        assertThatThrownBy(() -> weatherApiService.getListCitiesByName(city))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("API Error: 401");
        verify(httpClient).send(argThat(request ->
                request.uri().toString().contains("q=london")), any());
    }


    @Test
    void whenDontGetWeatherForUserByUserThenReturnEmptyList() {
        User user = null;
        when(locationService.getLocationByUser(null)).thenReturn(Collections.emptyList());
        when(weatherDtoMapper.map(anyMap())).thenReturn(Collections.emptyList());
        List<WeatherDto> result = weatherApiService.getWeatherForUser(user);
        assertThat(result).isEmpty();
        verify(locationService).getLocationByUser(null);
    }

    @Test
    void whenGetWeatherForUserByUserThenReturnListWeatherDto() {
        User user = new User();
        user.setLogin("login");
        Location location = new Location();
        location.setUser(user);
        location.setName("gomel");
        location.setLatitude(11.111);
        location.setLongitude(22.222);
        List<Location> locations = List.of(location);
        when(locationService.getLocationByUser(user)).thenReturn(locations);
        List<Location> result = locationService.getLocationByUser(user);
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(location);
        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();
        weatherResponseDto.setLocation(location.getName());
        WeatherData weatherData = new WeatherData();
        weatherData.setIcon("2d0");
        weatherData.setDescription("description");
        WeatherData[] weatherDataArray = new WeatherData[]{weatherData};
        weatherResponseDto.setWeatherData(weatherDataArray);
        MainData mainData = new MainData();
        mainData.setHumidity(70);
        mainData.setTemperature(3.3);
        weatherResponseDto.setMainData(mainData);
        CoordsDto coordsDto = new CoordsDto();
        coordsDto.setLatitude(location.getLatitude());
        coordsDto.setLongitude(location.getLongitude());
        weatherResponseDto.setCoordsDto(coordsDto);
        Map<Location, WeatherResponseDto> expectedMap = Map.of(location, weatherResponseDto);
        WeatherApiService spyService = spy(weatherApiService);
        doReturn(expectedMap).when(spyService).getWeatherResponseDtoMap(locations);
        Map<Location, WeatherResponseDto> resultWeatherMap = spyService.getWeatherResponseDtoMap(locations);
        assertThat(resultWeatherMap)
                .isNotEmpty()
                .hasSize(1);
        assertThat(resultWeatherMap.containsKey(location)).isTrue();
        assertThat(resultWeatherMap.containsValue(weatherResponseDto)).isTrue();
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setLocation(location.getName());
        weatherDto.setImage(weatherResponseDto.getImage());
        weatherDto.setDescription(weatherResponseDto.getDescription());
        weatherDto.setTemperature(weatherResponseDto.getTemperature());
        weatherDto.setHumidity(weatherResponseDto.getHumidity());
        when(weatherDtoMapper.map(resultWeatherMap)).thenReturn(List.of(weatherDto));
        List<WeatherDto> resultWeatherDto = weatherDtoMapper.map(resultWeatherMap);
        assertThat(resultWeatherDto)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(weatherDto);
    }

    @Test
    void whenGetFoundLocationDtosByCityAndNotFoundThenReturnEmptyList() {
        String city = "gay-city-chat";
        String jsonResponceMock = "[]";
        WeatherApiService spyService = spy(weatherApiService);
        doReturn(jsonResponceMock).when(spyService).getListCitiesByName(city);
        when(foundLocationDtoMapper.getFoundLocationDtoListFromStringJson(jsonResponceMock))
                .thenReturn(List.of());
        List<FoundLocationDto> result = spyService.getFoundLocationDtos(city);
        assertThat(result)
                .isNotNull()
                .isEmpty();
        verify(foundLocationDtoMapper, times(1)).getFoundLocationDtoListFromStringJson(jsonResponceMock);
    }

    @Test
    void whenGetFoundLocationDtosThenReturnParsedList() {
        String city = "gomel";
        String jsonResponceMock = "[{\n" +
                "        \"name\": \"Гомель\",\n" +
                "        \"local_names\": {\n" +
                "            \"lt\": \"Gomelis\",\n" +
                "            \"pl\": \"Homel\",\n" +
                "            \"ru\": \"Гомель\",\n" +
                "            \"ascii\": \"Homieĺ\",\n" +
                "            \"feature_name\": \"Homieĺ\",\n" +
                "            \"be\": \"Гомель\"\n" +
                "        },\n" +
                "        \"lat\": 55.2999833,\n" +
                "        \"lon\": 28.7762135,\n" +
                "        \"country\": \"BY\",\n" +
                "        \"state\": \"Vitsebsk Region\"\n" +
                "    }]";
        FoundLocationDto foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName("Гомель");
        foundLocationDto.setLat(55.2999833);
        foundLocationDto.setLon(28.7762135);
        foundLocationDto.setCountry("BY");
        foundLocationDto.setState("Vitsebsk Region");
        List<FoundLocationDto> expected = List.of(foundLocationDto);
        WeatherApiService spyService = spy(weatherApiService);
        doReturn(jsonResponceMock).when(spyService).getListCitiesByName(city);
        when(foundLocationDtoMapper.getFoundLocationDtoListFromStringJson(jsonResponceMock))
                .thenReturn(expected);
        List<FoundLocationDto> result = spyService.getFoundLocationDtos(city);
        assertThat(result)
                .isNotEmpty()
                .hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Гомель");
        verify(foundLocationDtoMapper, times(1)).getFoundLocationDtoListFromStringJson(anyString());
    }
}
