package main.java.app.service;

import java.io.IOException;
import java.util.Map;

/**
 * Interface for service that interacts with WeatherAPI.
 */
public interface WeatherService {
    /**
     * @param city Name of the city.
     * @return Key-value pairs object with data.
     * @throws IOException The Problem with getting a result occurred.
     */
    Map<String, String> getNextDayForecast(String city) throws IOException;
}
