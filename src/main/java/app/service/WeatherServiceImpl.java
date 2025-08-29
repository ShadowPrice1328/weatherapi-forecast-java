package main.java.app.service;

import com.google.gson.JsonObject;
import main.java.app.net.RetrofitClient;
import main.java.app.net.WeatherApi;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Service that interacts with WeatherAPI
 */
public class WeatherServiceImpl implements WeatherService {
    private final WeatherApi api;
    private final String apiKey;
    public WeatherServiceImpl(String apiKey) {
        this.api = RetrofitClient.getClient().create(WeatherApi.class);
        this.apiKey = apiKey;
    }
    @Override
    public Map<String, String> getNextDayForecast(String city) throws IOException {
        Response<JsonObject> response = api.getForecast(apiKey, city, 1).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw  new IOException("Failed to fetch forecast for " + city);
        }

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        JsonObject dayForecast = response.body();

        return Map.of();
    }
}
