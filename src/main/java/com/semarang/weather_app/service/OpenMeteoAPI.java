package com.semarang.weather_app.service;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;
import org.json.JSONObject; // Ini dari library yang baru kita tambah
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service // Menandakan class ini adalah Service Spring Boot
public class OpenMeteoAPI extends WeatherAPI implements IWeatherProvider {

    @Override
    public WeatherData getCurrentWeather(Location loc) {
        try {
            // UPDATE URL: Kita minta parameter lengkap (suhu, lembab, angin, kode cuaca)
            String url = "https://api.open-meteo.com/v1/forecast?latitude="
                    + loc.getLatitude() + "&longitude=" + loc.getLongitude()
                    + "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            // UPDATE PARSING: Datanya sekarang ada di blok "current"
            JSONObject cur = json.getJSONObject("current");

            WeatherData data = new WeatherData();
            data.setTemperature(cur.getDouble("temperature_2m"));
            data.setWindSpeed(cur.getDouble("wind_speed_10m"));
            data.setHumidity(cur.getDouble("relative_humidity_2m")); // NAH INI DIA!

            int code = cur.getInt("weather_code");
            data.setCondition(translateWeatherCode(code));

            // Tambahkan sedikit penanda sumber
            data.setCondition(data.getCondition() + " (OpenMeteo)");

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Kalau error (misal internet mati), kembalikan null
        }
    }

    // Helper method untuk menerjemahkan kode angka ke kata-kata
    private String translateWeatherCode(int code) {
        if (code == 0) return "Cerah ☀️";
        if (code >= 1 && code <= 3) return "Berawan ☁️";
        if (code >= 45 && code <= 48) return "Kabut 🌫️";
        if (code >= 51 && code <= 67) return "Gerimis 🌧️";
        if (code >= 71 && code <= 99) return "Hujan Badai ⛈️";
        return "Tidak diketahui";
    }
}