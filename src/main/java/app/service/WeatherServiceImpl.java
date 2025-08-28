package main.java.app.service;

import java.io.IOException;
import java.util.Map;

/**
 * Service that interacts with WeatherAPI
 */
public class WeatherServiceImpl implements WeatherService {
    @Override
    public Map<String, String> getNextDayForecast(String city) throws IOException {
        return Map.of();
    }
}
