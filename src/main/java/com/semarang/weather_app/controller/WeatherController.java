package com.semarang.weather_app.controller;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;
import com.semarang.weather_app.service.WeatherService;
import com.semarang.weather_app.utils.SemarangLocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService service;

    @GetMapping("/cuaca")
    public String getWeather(
            @RequestParam(name = "lokasi", required = false, defaultValue = "semarang_tengah") String lokasi,
            @RequestParam(name = "sumber", required = false, defaultValue = "openmeteo") String sumber,
            Model model){

        Location loc = SemarangLocationData.getLocation(lokasi);

        // Jika user tidak sengaja kirim sumber kosong, paksa default
        if (sumber == null || sumber.isEmpty()) {
            sumber = "openmeteo";
        }

        WeatherData data = service.getWeatherFor(loc, sumber);

        // JAGA-JAGA: Kalau service masih nekat balikin null (apes banget), kita buat dummy
        if (data == null) {
            data = new WeatherData(0, 0, 0, "System Error");
        }

        model.addAttribute("lokasi", loc.getName());
        model.addAttribute("data", data);
        model.addAttribute("riwayat", service.getHistory());

        // PENTING: Kirim balik string ini agar HTML tidak bingung
        model.addAttribute("pilihanSumber", sumber);

        return "cuaca";
    }
}