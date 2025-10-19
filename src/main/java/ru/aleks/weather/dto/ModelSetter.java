package ru.aleks.weather.dto;

public interface ModelSetter<T> {

    T modelSet(LocationTransform locationTransform, int userId);
}
