package app;

import app.service.WeatherService;
import app.service.WeatherServiceImpl;

import java.util.*;

public class WeatherApp {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("WEATHERAPI_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Please set WEATHERAPI_KEY environment variable.");
            return;
        }

        List<String> cities = Arrays.asList("Chisinau", "Madrid", "Kyiv", "Amsterdam");

        WeatherService weatherService = new WeatherServiceImpl(apiKey);

        Map<String, Map<String, Object>> forecastData = new LinkedHashMap<>();

        for (String city : cities) {
            Map<String, Object> data = weatherService.getNextDayForecast(city);
            forecastData.put(city, data);
        }

        printTable(forecastData);
    }

    private static void printTable(Map<String, Map<String, Object>> forecastData) {
        String format = "| %-12s | %-13s | %-13s | %-12s | %-17s | %-8s |%n";
        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");
        System.out.format(format, "City", "Min Temp (°C)", "Max Temp (°C)", "Humidity (%)", "Wind Speed (kph)", "Wind Dir");
        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");

        for (Map.Entry<String, Map<String, Object>> entry : forecastData.entrySet()) {
            Map<String, Object> d = entry.getValue();
            System.out.format(format,
                    entry.getKey(),
                    d.get("minTemp"),
                    d.get("maxTemp"),
                    d.get("humidity"),
                    d.get("windSpeed"),
                    d.get("windDir")
            );
        }

        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");
    }
}
