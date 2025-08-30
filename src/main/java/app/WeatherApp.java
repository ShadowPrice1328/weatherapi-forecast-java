package app;

import app.model.DayForecast;
import app.service.WeatherService;
import app.service.WeatherServiceImpl;

import java.util.*;

/**
 * Main class, entry point
 */
public class WeatherApp {
    private static final List<String> CITIES =
            Arrays.asList("Chisinau", "Madrid", "Kyiv", "Amsterdam");
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("WEATHERAPI_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Please set WEATHERAPI_KEY environment variable.");
            return;
        }

        WeatherService weatherService = new WeatherServiceImpl(apiKey);
        Map<String, DayForecast> forecastData = fetchForecasts(weatherService);

        printTable(forecastData);
    }

    private static Map<String, DayForecast> fetchForecasts(WeatherService service) {
        Map<String, DayForecast> data = new LinkedHashMap<>();
        for (String city : WeatherApp.CITIES) {
            try {
                data.put(city, service.getNextDayForecast(city));
            } catch (Exception e) {
                System.out.println("Failed to fetch data for " + city + ": " + e.getMessage());
            }
        }
        return data;
    }

    private static void printTable(Map<String, DayForecast> forecastData) {
        String format = "| %-12s | %-13s | %-13s | %-12s | %-17s | %-8s |%n";
        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");
        System.out.format(format, "City", "Min Temp (°C)", "Max Temp (°C)", "Humidity (%)", "Wind Speed (kph)", "Wind Dir");
        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");

        for (Map.Entry<String, DayForecast> entry : forecastData.entrySet()) {
            DayForecast d = entry.getValue();
            System.out.format(format,
                    entry.getKey(),
                    String.format("%.1f", d.getMinTemp()),
                    String.format("%.1f", d.getMaxTemp()),
                    d.getHumidity(),
                    String.format("%.1f", d.getWindSpeed()),
                    d.getWindDir()
            );
        }

        System.out.format("+--------------+---------------+---------------+--------------+-------------------+----------+%n");
    }
}
