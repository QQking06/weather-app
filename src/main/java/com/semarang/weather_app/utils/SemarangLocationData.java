package com.semarang.weather_app.utils;

import com.semarang.weather_app.model.Location;
import java.util.HashMap;
import java.util.Map;

public class SemarangLocationData {

    // Kita pakai Map biar gampang cari data berdasarkan nama
    private static final Map<String, Location> data = new HashMap<>();

    static {
        // Format: Nama Kecamatan, Latitude, Longitude
        data.put("banyumanik", new Location("Banyumanik", -7.056, 110.420));
        data.put("gunungpati", new Location("Gunungpati", -7.098, 110.392));
        data.put("semarang_tengah", new Location("Semarang Tengah", -6.980, 110.419));
        data.put("tembalang", new Location("Tembalang", -7.049, 110.441));
        data.put("pedurungan", new Location("Pedurungan", -7.005, 110.463));
        data.put("ngaliyan", new Location("Ngaliyan", -7.001, 110.347));
    }

    public static Location getLocation(String kecamatan) {
        // Ubah jadi huruf kecil semua biar tidak error kalau user ngetik "Banyumanik" atau "banyumanik"
        String key = kecamatan.toLowerCase().trim();

        // Kalau kecamatan tidak ditemukan, default ke Semarang Tengah
        return data.getOrDefault(key, data.get("semarang_tengah"));
    }
}