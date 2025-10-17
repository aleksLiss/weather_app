package ru.aleks.weather.utils;

import ru.aleks.weather.dto.LocationSendDto;
import ru.aleks.weather.model.Location;

public interface ObjectsTransformer {

    public <T extends Location, R extends LocationSendDto> R transformTo(T object);
}
