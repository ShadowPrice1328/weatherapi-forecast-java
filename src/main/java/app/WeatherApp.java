package app;

import app.model.DayForecast;
import app.service.WeatherService;
import app.service.WeatherServiceImpl;
import app.util.WeatherTablePrinter;

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

        WeatherTablePrinter.print(forecastData);
    }

    /**
     * Fetching forecast for each city
     * @param service Service that fetches data
     * @return Fetched data presented as Map
     */
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
}
