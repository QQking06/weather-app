package com.semarang.weather_app.service;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    // Kita panggil dua-duanya
    @Autowired private OpenMeteoAPI openMeteo;
    @Autowired private BmkgAPI bmkg;

    private List<String> searchHistory = new ArrayList<>();

    // Method kini menerima parameter 'source'
    public WeatherData getWeatherFor(Location loc, String source){

        WeatherData data = null;

        // --- POLYMORPHISM IN ACTION ---
        // Variabel 'provider' tipenya Interface, tapi isinya bisa OpenMeteo atau BMKG
        IWeatherProvider provider;

        if (source.equalsIgnoreCase("bmkg")) {
            provider = bmkg; // Pakai Otak BMKG
        } else {
            provider = openMeteo; // Pakai Otak OpenMeteo
        }

        // Panggil method yang sama, tapi perilaku beda
        data = provider.getCurrentWeather(loc);

        // Catat history dengan label sumbernya
        addToHistory(loc.getName() + " via " + source.toUpperCase());

        return data;
    }

    private void addToHistory(String locationName) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        searchHistory.add(0, locationName + " (" + time + ")");
        if (searchHistory.size() > 5) searchHistory.remove(searchHistory.size() - 1);
    }

    public List<String> getHistory() { return searchHistory; }
}