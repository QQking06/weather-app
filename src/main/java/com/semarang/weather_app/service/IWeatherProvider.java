package com.semarang.weather_app.service;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;

public interface IWeatherProvider {
    // Siapa pun yang implement ini, WAJIB bisa mengambil data cuaca
    WeatherData getCurrentWeather(Location location);
}