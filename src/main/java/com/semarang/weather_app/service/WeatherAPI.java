package com.semarang.weather_app.service;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;

// Abstract class tidak bisa dibuat objectnya langsung (new WeatherAPI() -> Error)
public abstract class WeatherAPI {

    // Abstract method: cuma judulnya saja, isinya terserah class anaknya nanti
    public abstract WeatherData getCurrentWeather(Location location);
}