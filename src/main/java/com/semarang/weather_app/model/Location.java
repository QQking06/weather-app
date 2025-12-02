package com.semarang.weather_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Ini otomatis membuat Getter & Setter (Enkapsulasi)
@AllArgsConstructor // Ini membuat Constructor dengan semua parameter
@NoArgsConstructor  // Ini membuat Constructor kosong
public class Location {
    private String name;      // Nama Kecamatan
    private double latitude;  // Garis Lintang
    private double longitude; // Garis Bujur
}