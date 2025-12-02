package com.semarang.weather_app.service;

import com.semarang.weather_app.model.Location;
import com.semarang.weather_app.model.WeatherData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class BmkgAPI extends WeatherAPI implements IWeatherProvider {

    private static final String BMKG_URL = "https://data.bmkg.go.id/DataMKG/MEWS/DigitalForecast/DigitalForecast-JawaTengah.xml";

    @Override
    public WeatherData getCurrentWeather(Location loc) {
        System.out.println("--- JSOUP FETCH BMKG V4 (SIMPLE) ---");
        try {
            // 1. KONEKSI SEDERHANA (Hapus header aneh-aneh penyebab error)
            Connection connect = Jsoup.connect(BMKG_URL)
                    .ignoreContentType(true) // Baca file apapun (XML/HTML)
                    .ignoreHttpErrors(true)
                    .timeout(10000)
                    .parser(Parser.xmlParser()) // Mode XML
                    .maxBodySize(0) // Unlimited size (biar gak kepotong)
                    // PENTING: User Agent wajib ada biar dikira Browser
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

            // CATATAN: Saya menghapus 'Accept-Encoding' agar server TIDAK mengirim file ZIP.

            Document doc = connect.get();

            // Cek apakah data terbaca teks normal?
            String preview = doc.text();
            if (preview.length() > 100) preview = preview.substring(0, 100);
            System.out.println("Preview Data: " + preview);

            // 2. Cek Total Area
            Elements allAreas = doc.select("area");
            System.out.println("Total Tag <area>: " + allAreas.size());

            if (allAreas.isEmpty()) {
                throw new Exception("XML Kosong/Gagal (Cek Preview Data di atas)");
            }

            Element areaSemarang = null;

            // 3. Loop Pencarian (Logika Cerdas)
            for (Element area : allAreas) {
                // BMKG kadang pakai description="Semarang", kadang names="Semarang"
                String desc = area.attr("description");

                // Cari "Semarang" tapi hindari "Kabupaten"
                if (desc.toLowerCase().contains("semarang") && !desc.toLowerCase().contains("kab")) {
                    System.out.println("✅ KETEMU KOTA: " + desc);
                    areaSemarang = area;
                    break;
                }
            }

            // Fallback (Jaga-jaga)
            if (areaSemarang == null) {
                for (Element area : allAreas) {
                    if (area.attr("description").toLowerCase().contains("semarang")) {
                        areaSemarang = area;
                        System.out.println("⚠️ Fallback ke: " + area.attr("description"));
                        break;
                    }
                }
            }

            if (areaSemarang == null) throw new Exception("Kota Semarang tidak ditemukan di XML.");

            // 4. Ambil Data
            double temp = getVal(areaSemarang, "t");
            double humid = getVal(areaSemarang, "hu");
            double wind = getVal(areaSemarang, "ws");

            WeatherData data = new WeatherData();
            data.setTemperature(temp);
            data.setHumidity(humid);
            data.setWindSpeed(wind);
            data.setCondition("Cerah Berawan (BMKG: " + areaSemarang.attr("description") + ")");

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            WeatherData errorData = new WeatherData();
            errorData.setCondition("Gagal: " + e.getMessage());
            return errorData;
        }
    }

    private double getVal(Element area, String paramId) {
        try {
            // Cari parameter ID
            Element param = area.select("parameter[id=" + paramId + "]").first();
            if (param == null) return 0;

            // Ambil value pertama (index 0)
            Element val = param.select("timerange value").first();
            if (val == null) return 0;

            return Double.parseDouble(val.text());
        } catch (Exception e) {
            return 0;
        }
    }
}