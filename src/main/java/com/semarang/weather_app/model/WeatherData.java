package com.semarang.weather_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {
    private double temperature;
    private double windSpeed;
    private double humidity;
    private String condition;

    // --- FITUR TAMBAHAN: LOGIKA CERDAS (METHOD) ---

    // Method untuk menentukan apakah cuaca ekstrem
    public boolean isExtreme() {
        return temperature > 34.0 || windSpeed > 20.0;
    }

    // Method untuk memberikan saran ke manusia
    public String getRecommendation() {
        if (condition.toLowerCase().contains("hujan") || condition.toLowerCase().contains("gerimis")) {
            return "🌧️ Bawa payung atau jas hujan, bro!";
        } else if (temperature > 33.0) {
            return "🥵 Panas pol! Pakai sunscreen dan minum air.";
        } else if (windSpeed > 15.0) {
            return "🍃 Angin kencang, hati-hati kalau naik motor.";
        } else {
            return "✅ Cuaca aman untuk nongkrong.";
        }
    }

    // Method untuk menentukan warna background CSS
    public String getCardColor() {
        if (condition.toLowerCase().contains("hujan")) return "linear-gradient(135deg, #a8c0ff 0%, #3f2b96 100%)"; // Biru Hujan
        if (temperature > 30.0) return "linear-gradient(135deg, #f6d365 0%, #fda085 100%)"; // Orange Panas
        return "linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%)"; // Biru Cerah
    }
}